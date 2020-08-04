package Download_Manager;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import static org.junit.Assert.*;

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