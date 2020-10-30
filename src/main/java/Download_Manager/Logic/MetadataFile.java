package Download_Manager.Logic;

/**
 * This class represents the metadata object that will write in to the disk with the name of the file and the TEMP_SUFFIX.
 * The metadata is countin (1) chunksUntilNow = chunks that already downloaded, (2) size = how many chunk is needed
 * (3) bitMap = boolean bit map that represents the chunk how already finish to downloaded,
 * (4) fileName = the name of the file
 */
public class MetadataFile implements java.io.Serializable{
    private long chunksUntilNow;
    private long size;
    private boolean [] bitMap;
    private String fileName; // For check if this is the file how already download

    /**
     * Constructor of the MetadataFile
     * @param size - the size of the metadata
     * @param fileName - the name of the file
     * @param chunkSize - the chunk size
     */
    public MetadataFile(long size, String fileName, int chunkSize){
        this.size = size / chunkSize;
        if(size % chunkSize != 0) this.size++;

        this.bitMap = new boolean[(int) this.size];
        this.chunksUntilNow = 0; // How many chunk are downloaded and written to the disk
        this.fileName = fileName;
    }

    /**
     *  Get chunks until now
     * @return chunks until now
     */
    public long getChunksUntilNow() {
        return chunksUntilNow;
    }

    /**
     * Get size of the file
     * @return size of the file
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the bit map
     * @return the bit map that array of  boolean
     */
    public boolean[] getBitMap() {
        return bitMap;
    }

    /**
     * Get the name of the file
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *  Set the bit map array with the current index to true
     * @param index - index on the bit map
     */
    public void setBitMap(int index) {
        this.bitMap[index] = true;
        this.chunksUntilNow ++;
    }

    /**
     * Get the percentages in integer that already completed
     * @return the percentages that already completed in integer
     */
    public int getPercentages(){
        return (int)(((float) chunksUntilNow / size) * 100);
    }

    /**
     * Return how many chunks needed to complete the downloader
     * @return int of how many chunks needed to complete
     */
    public long getStillChunksToComplet(){
        return this.size - this.chunksUntilNow;
    }

    /**
     *
     * @return to string of the metadata file
     */
    @Override
    public String toString() {
        return "MetaData: chunksUntilNow- " + chunksUntilNow + " size- " + size + " bitMapSize- " + bitMap.length +
                " Percentages- " + getPercentages();
    }
}