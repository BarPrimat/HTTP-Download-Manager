package Download_Manager;

/**
 * This class define a chunk
 */
public class DataChunk {
    private byte[] data; // The data in byte
    private int id; // The position of the chank in the Bitmap
    private long offset; // Byte offset

    /**
     * Constructor
     * @param data - the data of the chunk in bytes
     * @param id - index of the chunk
     * @param offset - offset of the chunk
     */
    public DataChunk(byte [] data, int id, long offset){
        this.data = data;
        this.id = id;
        this.offset = offset;
    }

    /**
     *
     * @return the data in byte array
     */
    public byte[] getData() {
        return data;
    }

    /**
     *
     * @return chunk id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return offset of the chunk
     */
    public long getOffset() {
        return offset;
    }

    /**
     *
     * @return to string of the chunk
     */
    @Override
    public String toString() {
        return "DataChunk: id- " + id + " offset- " + offset + " dataLength- " + data.length;
    }
}
