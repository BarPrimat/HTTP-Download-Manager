/**
 * This class represents a static variable
 */
public class StaticVariable {
    public final static int READ_TIMEOUT_MESSAGE = 1000 * 30; // 30 seconds
    public final static int CONNECT_TIMEOUT_MESSAGE = 1000 * 30; // 30 seconds
    public final static int CHUNK_SIZE = 1024 * 100; // 100KB
    public final static String TEMP_SUFFIX = ".tmp"; // The suffix name of the temporary file that containing the Metadata

    public final static int MINIMUM_BYTE_FOR_START = (int) Math.pow(2, 20); // 1 MB
    public final static int BLOCKING_QUEUE_CAPACITY = 500; // The maximum of the capacity in the blocking queue
    public final static int nDAYS = 2; // Assume the download will finish until 2 days
    public final static int TIMEOUT_CHUNK = 2; // In minutes
    public final static int DEFAULT_NUMBER_OF_THREADS = 1; // If there is no input of number threads the default will be 1
}

