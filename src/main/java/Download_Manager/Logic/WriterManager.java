package Download_Manager.Logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.*;

/**
 * This class responsible for writing the data and tge temporary metadata file on the disk
 * and transfer the 2 temporary metadat file in to 1 (with ATOMIC_MOVE) that for saving the metadata file in case of
 * problem with writing on the disk
 */

public class WriterManager {

    private ManagerDownloader managerDownloader;
    private MetadataFile metadataFile;
    private RandomAccessFile file;
    private String tempFileName;
    private static String pathToWriteTheFile;

    public WriterManager(String fileName, long size, ManagerDownloader managerDownloader){
        this.tempFileName = String.format("%s%s%s", pathToWriteTheFile, fileName ,StaticVariable.TEMP_SUFFIX);
        try {
            this.file = new RandomAccessFile(fileName , "rw");
            // Now the file which will download into is create
            File tempFile = new File(tempFileName); // Open the temp File
            this.managerDownloader = managerDownloader;
            if(!tempFile.exists()) this.metadataFile = new MetadataFile(size, fileName , StaticVariable.CHUNK_SIZE);
            else {
                // The metadata file is already exists
                // Open the metadata file
                ObjectInputStream objectFile = new ObjectInputStream(new FileInputStream(tempFile));
                this.metadataFile = (MetadataFile) objectFile.readObject();
                objectFile.close();
            }
        }catch (IOException | ClassNotFoundException e) {
            System.err.println("There is some problem with the metadata file");
            return;
        }
    }

    public static void setPathToWriteTheFile(String pathToWriteTheFile) {
        WriterManager.pathToWriteTheFile = pathToWriteTheFile;
    }

    public void writeToFile(DataChunk dataChunk){
        try {
            int previousPercentages = this.metadataFile.getPercentages();
            if(previousPercentages == 100) return;
            // Seek the offset in the file and write the data into the correct index of the file
            this.file.seek(dataChunk.getOffset());
            this.file.write(dataChunk.getData());

            this.metadataFile.setBitMap(dataChunk.getId()); // Update the bit map
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(this.tempFileName + "1"));
            objectOut.writeObject(this.metadataFile);

            objectOut.close();

            File nowTempMetadata = new File(this.tempFileName); // new name
            File prevTempMetadata = new File(this.tempFileName + "1"); // old name
            // Now we will use atomic operations to ensure that the information which we wrote will not have any problem
            // when any malfunction occurs because the operation is atomic
            try {
                Files.move(prevTempMetadata.toPath(), nowTempMetadata.toPath(), StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException ignored) {}
            int currentPercentages = this.metadataFile.getPercentages();
            // Check if the percentages is change after this adding
            if(previousPercentages < currentPercentages){
                System.out.println("Downloaded " + currentPercentages + "%");
            }
        } catch (IOException e) {
            System.err.println("There is problem with write to the file");
        }
    }

    /**
     *  Return the Metadata
     * @return
     */
    public MetadataFile getMetadataFile() {
        return this.metadataFile;
    }

    /**
     *
     * @return the managerDownloader
     */
    public ManagerDownloader getManagerDownloader() {
        return managerDownloader;
    }

    /**
     *
     * @param bq
     * @return - new thread that will called finally to writeToFile
     */
    public Thread createWorkerThread(BlockingQueue<DataChunk> bq){
        return new Thread(new FileWriter(this, bq));
    }

    /**
     * the function deleted the Metadata file from the disk after finish download
     */
    public void deleteMetadataFile() {
        String tempFile = this.tempFileName;

        File file = new File(tempFile);
        if (!file.delete()) {
            System.err.println("can't delete the Metadata file: " + tempFile);
        }
        if (this.file != null) {
            try {
                this.file.close();
            } catch (IOException e) {
                System.err.println("Can't close the file: " + this.file);
            }
        }
    }

    /**
     * Inner class
     * This class responsible to put out the data from the BlockingQueue and write it on the disk
     */
    public class FileWriter implements Runnable {

        private BlockingQueue<DataChunk> bq;
        private WriterManager writerManager;

        /**
         * constructor for fileWriter
         * @param writerManager - the creator for this function
         * @param bq
         */
        public FileWriter(WriterManager writerManager, BlockingQueue<DataChunk> bq) {
            this.writerManager = writerManager;
            this.bq = bq;
        }

        /**
         * Put out the data from the BlockingQueue and write it on the disk
         */
        public void run() {
            long chunksRemainder = this.writerManager.getMetadataFile().getStillChunksToComplet();
            for (int i = 0; i < chunksRemainder; i++) {
                try {
                    // After TIMEOUT_CHUNK minutes the there is error of timeout (in our case after 2 minutes)
                    DataChunk dataChunk = this.bq.poll(StaticVariable.TIMEOUT_CHUNK, TimeUnit.MINUTES);
                    if (dataChunk != null)
                        this.writerManager.writeToFile(dataChunk);
                    else{
                        this.writerManager.getManagerDownloader().kill(new Exception("Waiting too mach time for a single chunk"));
                        break;
                    }
                } catch (InterruptedException e) {
                    this.writerManager.getManagerDownloader().kill(e);
                    return;
                }
            }
        }
    }
}