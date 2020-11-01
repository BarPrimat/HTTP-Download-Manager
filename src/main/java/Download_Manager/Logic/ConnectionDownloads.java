package Download_Manager.Logic;

import Download_Manager.UI.Display;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;

/**
 * This class is Runnable thread for download the data from the given URL in the range that the creator given to this thread
 */

public class ConnectionDownloads implements Runnable{

    private URL url;
    private boolean[] bitmap;
    private int id;
    private int rangeOfStart;
    private int rangeOfEnd;
    private int chunkAmount;
    private  BlockingQueue<DataChunk> queue;
    private int numberOfConnection;
    private HttpURLConnection connection;
    private ManagerDownloader managerDownloader;

    /**
     * constructor
     * @param id - index
     * @param url - url address that will download from him
     * @param rangeOfStart - the range that function starting the download
     * @param rangeOfEnd - the range that function ending the download
     * @param bq - blocking queue that will receive chunks
     * @param bitmap - the bitMap that represent the map of the chunks that already successful downloaded
     * @param chunkAmount - amount of chunk
     * @param numberOfConnection - number of connection
     * @param managerDownloader - the create of this thread
     */
    public ConnectionDownloads(int id, String url, int rangeOfStart, int rangeOfEnd, BlockingQueue<DataChunk> bq,
                               boolean[] bitmap, int chunkAmount, int numberOfConnection, ManagerDownloader managerDownloader) {
        this.id = id;
        this.rangeOfStart = rangeOfStart;
        this.rangeOfEnd = rangeOfEnd;
        this.bitmap = bitmap;
        this.queue = bq;
        this.chunkAmount = chunkAmount;
        this.numberOfConnection = numberOfConnection;
        this.managerDownloader = managerDownloader;

        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Display.printError("There is problem with URL");
            this.managerDownloader.kill(e);
        }
    }

    /**
     * the run is override function of Runnable.
     * the function is responsible of downloading from the given url, range and put the chunk in to the Blocking Queue
     */
    @Override
    public void run() {
        if(this.rangeOfStart >= this.rangeOfEnd ){
            Display.print("[" + this.id + "]" + " Finished downloading. The range is already downloaded");
            return;
        }
        // Message to user
        String startRange = ("[%d] Start downloading range (%d - %d) from\n%s");
        startRange = String.format(startRange, this.id , this.rangeOfStart, this.rangeOfEnd, this.url.toString());
        Display.print(startRange);

        InputStream inputStream = null;
        try {
            this.connection = (HttpURLConnection) this.url.openConnection();
            this.connection.setRequestMethod("GET");

            // Setting range
            String range = String.format("Bytes=%d-%d", this.rangeOfStart, this.rangeOfEnd);
            this.connection.setRequestProperty("Range", range);
            this.connection.setConnectTimeout(StaticVariable.CONNECT_TIMEOUT_MESSAGE);
            this.connection.setReadTimeout(StaticVariable.READ_TIMEOUT_MESSAGE);
            this.connection.connect();

            // Downloading now
            int fileOffset;
            int readUntil = StaticVariable.CHUNK_SIZE;
            int startIndex = this.rangeOfStart / StaticVariable.CHUNK_SIZE;
            int endIndex = startIndex + this.chunkAmount;
            inputStream = this.connection.getInputStream();

            for (int i = startIndex; i < endIndex; i++){
                int offset = 0;
                int size = 0;
                fileOffset = readUntil * i;
                byte[] chunk = new byte[StaticVariable.CHUNK_SIZE]; // The allocate of the chunk buffer
                if((i == endIndex - 1) && (id == numberOfConnection - 1)){ // The last chunk so we need to set the chunk array size
                    chunk = new byte[(this.rangeOfEnd + 1) - fileOffset];
                    readUntil = chunk.length;
                }
                if(!this.bitmap[i]) { // If we not yet downloaded the data in this index
                    while(size != -1 && offset < readUntil) {
                        int length = readUntil - offset;
                        size = inputStream.read(chunk, offset, length);
                        offset += size;
                    }
                    DataChunk dataChunk = new DataChunk(chunk, i, fileOffset);
                    this.queue.put(dataChunk);
                }else {
                    // For case we already downloaded the data ade we need to skip until we so data that will not already downloaded
                    while (StaticVariable.CHUNK_SIZE > offset){
                        size = (int) inputStream.skip(readUntil);
                        offset += size;
                    }
                    // continue for next iteration for new chunk
                }
            }

            // Finished downloading with this thread
            Display.print("[" + this.id + "] Finished downloading");

        } catch (IOException e) {
            this.managerDownloader.kill(e);
        } catch (InterruptedException e) {
            // Display.printError("Problem with the use the put function BlockingQueue");
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Display.printError("Some problem with close the file");
                }
            }
        }
    }
}