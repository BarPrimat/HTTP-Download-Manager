package Download_Manager.Logic;

import Download_Manager.UI.Display;

import java.util.List;
import java.util.concurrent.*;

/**
 * This class is downloading file from list of URL in to file that call fileName with the suffix using
 * the given number of the threads
 */

public class ManagerDownloader {

    private ExecutorService threadPool;
    private boolean isManagerStillRunning;

    public ManagerDownloader (){}

    /**
     * The function downloading file from list of URL in to file that call fileName with the suffix using
     * the given number of the threads
     *
     * @param listOfURL - list of URL
     * @param fileName - the name of the file
     * @param sizeOfFile - the size of file
     * @param numberOfThreads - number of threads
     */
    public void manager(List<String> listOfURL, String fileName, long sizeOfFile, int numberOfThreads) {
        int numberOfConnection;
        isManagerStillRunning = true;
        // Limits the number of connection
        if ((sizeOfFile / numberOfThreads) < StaticVariable.MINIMUM_BYTE_FOR_START) {
            numberOfConnection = (int) (sizeOfFile / StaticVariable.MINIMUM_BYTE_FOR_START);
            Display.print("The minimum range per connection is " + StaticVariable.MINIMUM_BYTE_FOR_START);
            Display.print("Making efficient the connection number to " + numberOfConnection);
        } else {
            numberOfConnection = numberOfThreads;
        }

        // Print to user how mach connection
        if (numberOfConnection >= 2) Display.print("Downloading using " + numberOfConnection + " connections...");
        // Create WriterManager for support resume downloading
        WriterManager fileWriterManager = new WriterManager(fileName, sizeOfFile, this);

        BlockingQueue<DataChunk> bq = new ArrayBlockingQueue<>(StaticVariable.BLOCKING_QUEUE_CAPACITY);
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfConnection + 1);

        // Divide the downloads in to thread
        ConnectionDownloads[] connection = new ConnectionDownloads[numberOfConnection];
        int numberOfChunks =  (int) sizeOfFile / StaticVariable.CHUNK_SIZE;
        if((int) sizeOfFile % StaticVariable.CHUNK_SIZE != 0) numberOfChunks++; // If there is chunk there are smaller the the CHUNK_SIZE
        int chunksPerConnection = numberOfChunks / numberOfConnection;
        boolean[] bitMap = fileWriterManager.getMetadataFile().getBitMap();
        int rangeOfStart = 0;

        for(int i = 0; i < numberOfConnection; i++){
            // We have some data that already downloaded for resume that we will reduce the range to avoid download data
            // that we already downloaded
            int rangeStartOfConnection = rangeOfStart;
            int rangeEnd, limitedChunks;
            // Probably the last chunk of the last connection is less the CHUNK_SIZE
            if(i == (numberOfConnection - 1)){
                rangeEnd = (int) sizeOfFile - 1;
                limitedChunks = numberOfChunks - (chunksPerConnection * i);
            }else {
                rangeEnd = rangeOfStart + (StaticVariable.CHUNK_SIZE * chunksPerConnection) - 1;
                limitedChunks = chunksPerConnection;
            }
            int startIndexBitMap = rangeOfStart / StaticVariable.CHUNK_SIZE;
            for(int k = startIndexBitMap; k < startIndexBitMap + chunksPerConnection; k++){
                if(!bitMap[k]) break;
                rangeStartOfConnection += StaticVariable.CHUNK_SIZE;
                limitedChunks--;
            }
            connection[i] = new ConnectionDownloads(i, listOfURL.get(i % listOfURL.size()), rangeStartOfConnection,
                    rangeEnd, bq, bitMap, limitedChunks, numberOfConnection, this);
            threadPool.execute(connection[i]);
            rangeOfStart = rangeEnd + 1;
        }
        // Ended of the split

        // Create a single writer thread that charge of data from the blocking queue
        Thread fileWriterThread = fileWriterManager.createWorkerThread(bq);
        threadPool.execute(fileWriterThread);
        // Set the thread pool
        this.setThreadPool(threadPool);
        threadPool.shutdown();
        // Assume that the program finish the download before n days in our case 2 days
        try {
            this.threadPool.awaitTermination(StaticVariable.nDAYS, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            this.threadPool.shutdownNow();
            e.printStackTrace();
        }

        if(isManagerStillRunning) fileWriterManager.deleteMetadataFile();
    }

    /**
     *  Set the thread pool
     * @param threadPool
     */
    public void  setThreadPool(ExecutorService threadPool){
        this.threadPool = threadPool;
    }

    /**
     * Shutdown the running because some problem with download and the download is failed
     * @param e - Exception form the problem source
     */
    public synchronized void kill(Exception e) {
        if(e.getMessage().equals("Pause in purpose")) Display.printError(e.getMessage());
        Display.print("Download failed.");
        isManagerStillRunning = false;
        if (this.threadPool != null) {
            this.threadPool.shutdownNow(); // Ending all threads
        }
        // The tasks ignore the interrupts that create because of shutDownNow.
        // System.exit(1);
    }
}