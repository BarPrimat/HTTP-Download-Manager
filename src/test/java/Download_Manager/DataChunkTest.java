package Download_Manager;

import Download_Manager.Logic.DataChunk;
import junit.framework.TestCase;
import org.junit.Test;

public class DataChunkTest extends TestCase {
    byte [] data = new byte[100];
    int id = 50;
    long offset = 30;
    DataChunk dataChunk = new DataChunk(data, id, offset);

    @Test
    public void testToString() {
        String actual = dataChunk.toString();
        String expected = String.format("DataChunk: id- %1$d offset- %2$d dataLength- %3$d", id, offset, data.length);
        assertEquals(expected, actual);
    }
}