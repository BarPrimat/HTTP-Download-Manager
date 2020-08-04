package Download_Manager;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class MetadataFileTest extends TestCase {
    MetadataFile metadataFile;

    @Before
    public void setUp() throws Exception {
        long size = 200;
        String fileName = "MetadataFileTest";
        int chunkSize = 10;
        metadataFile = new MetadataFile(size, fileName, chunkSize);
    }

    @Test
    public void getChunksUntilNow() {
        for(int i = 0; i < 6; i++) metadataFile.setBitMap(i);
        int expected = 6;
        assertEquals("testGetChunksUntilNow:",expected, metadataFile.getChunksUntilNow());

    }

    @Test
    public void testGetStillChunksToComplet() {
        getChunksUntilNow();
        int expected = 14;
        assertEquals("testGetStillChunksToComplet:",expected, metadataFile.getStillChunksToComplet());
    }

    @Test
    public void testToString() {
        long chunksUntilNowExpected = 6;
        long sizeExpected = 20;
        int bitMapLengthExpected = (int) sizeExpected;
        int getPercentagesExpected = 30;

        getChunksUntilNow();

        String actual = metadataFile.toString();
        String expected = String.format("MetaData: chunksUntilNow- %1$d size- %2$d bitMapSize- %3$d Percentages- %4$d"
                , chunksUntilNowExpected, sizeExpected, bitMapLengthExpected, getPercentagesExpected);
        assertEquals("testToString:", expected, actual);
    }
}