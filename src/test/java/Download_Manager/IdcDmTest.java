package Download_Manager;

import Download_Manager.UI.IdcDm;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class IdcDmTest extends TestCase {
    String[] argsListNumber1;
    String[] argsListNumber2;
    String linkForURLNumber1;
    String linkForURLNumber2;


    @Before
    public void setUp() throws Exception {
        argsListNumber1 = new String[]{"CentOS-6.10-x86_64-netinstall.iso.list", "8"};
        argsListNumber2 = new String[]{"https://archive.org/download/Mario1_500/Mario1_500.avi", "6"};
        argsListNumber1[0] = String.format("%1$s%2$s%3$s",Paths.get("").toAbsolutePath().toString(), "\\src\\test\\resources\\", argsListNumber1[0]);

        linkForURLNumber1 = "http://centos.mirror.garr.it/centos/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso";
        linkForURLNumber2 = "https://archive.org/download/Mario1_500/Mario1_500.avi";
    }

    /**
     *  Test all the project with download the Mario1_500.avi file and check if the checksum(from the relevant MessageDigest algorithms)
     *  is the same that expected
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @Test
    public void testMain() throws NoSuchAlgorithmException, IOException {
        // [0] = fileName , [1] = checksum that expected, [2] = MessageDigest algorithms , [3] = isFromResources
        String [][]  arrOfTestes = {{"CentOS-6.10-x86_64-netinstall.iso", "36b9b5d36d3a61b541e64b01eb9734667235615e", "SHA-1", "true"},
                                    {"Mario1_500.avi", "229c0c5a0d1b797ce1e0ce2557e93e3b","MD5", "false"}};
        String [] messageDigestAlgorithms = {"MD5", "SHA-1", "SHA-256"}; // Can deal with these algorithms that by definition from MessageDigest class

        downloadFileAndCheckIt(argsListNumber1, arrOfTestes[0][0], arrOfTestes[0][1], arrOfTestes[0][2], Boolean.parseBoolean(arrOfTestes[0][3]));
        downloadFileAndCheckIt(argsListNumber2, arrOfTestes[1][0], arrOfTestes[1][1], arrOfTestes[1][2], Boolean.parseBoolean(arrOfTestes[1][3]));
    }

    private void downloadFileAndCheckIt(String[] argsList, String filename, String expectedChecksum, String messageDigestAlgorithms, boolean isFromResources) throws NoSuchAlgorithmException, IOException {
        IdcDm.main(argsList);
        MessageDigest md = MessageDigest.getInstance(messageDigestAlgorithms);
        String filenameWithPath;
        if(isFromResources) filenameWithPath = String.format("%1$s%2$s%3$s",Paths.get("").toAbsolutePath().toString(), "\\src\\test\\resources\\", filename);
        else filenameWithPath = String.format("%1$s%2$s%3$s",Paths.get("").toAbsolutePath().toString(), "\\", filename);

        md.update(Files.readAllBytes(Paths.get(filenameWithPath)));
        byte[] digest = md.digest();
        String actualFromDownloadFile = byteArrayToHex(digest);
        assertEquals("testMain:", expectedChecksum, actualFromDownloadFile);
        deletedFile(filenameWithPath);
    }


    private void deletedFile(String filenameWithPath) {
        File testFile = new File(filenameWithPath);
        if (testFile.delete()) {
            System.out.println("Deleted the file: " + testFile.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    private String byteArrayToHex(byte[] digest) {
        StringBuilder builder = new StringBuilder();
        for(byte byteToCovered : digest) {
            builder.append(String.format("%02x", byteToCovered));
        }
        return  builder.toString();
    }

    @Test
    public void testListOfURL() {
        List<String> actualListOfURL = IdcDm.listOfURL(argsListNumber1[0]);
        List<String> expectedListOfURL = listThatExpected();
        assertEquals("testListOfURL:", expectedListOfURL, actualListOfURL);
    }

    private List<String> listThatExpected(){
        List<String> expectedListOfURL = new ArrayList<>();
        expectedListOfURL.add("http://centos.mirror.garr.it/centos/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso");
        expectedListOfURL.add("http://centos.activecloud.co.il/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso");
        expectedListOfURL.add("http://mirror.nonstop.co.il/centos/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso");
        expectedListOfURL.add("http://centos.aumix.net/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso");
        expectedListOfURL.add("http://mirror.isoc.org.il/pub/centos/6.10/isos/x86_64/CentOS-6.10-x86_64-netinstall.iso");
        return expectedListOfURL;
    }

    @Test
    public void testSizeOfFile() throws IOException {
        long actualSizeOfFileNumber1 = IdcDm.sizeOfFile(linkForURLNumber1);
        long expectedSizeOfFileNumber1 = 240123904;
        assertEquals("sizeOfFile:", expectedSizeOfFileNumber1, actualSizeOfFileNumber1);

        long actualSizeOfFileNumber2 = IdcDm.sizeOfFile(linkForURLNumber2);
        long expectedSizeOfFileNumber2 = 24334492;
        assertEquals("sizeOfFile:", expectedSizeOfFileNumber2, actualSizeOfFileNumber2);
    }
}