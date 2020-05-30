package Download_Manager;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class is the main class for Download_Manager
 * the class received URL or list od URL and number of thread and start downloading from the given input
 */

public class IdcDm {
    public static void main(String[] args) {
        String fileName = null;
        int numberOfThreads = StaticVariable.DEFAULT_NUMBER_OF_THREADS; // If there is no input of number threads the default will be 1
        long sizeOfFile = 0;
        List<String> listOfURL = null;

        if (args.length >= 3 || args.length == 0) {
            System.err.println("Usage:\n\tjava IdcDm URL|URL-LIST-FILE [MAX-CONCURRENT-CONNECTIONS]");
            return;
        }
        try {
            listOfURL = listOfURL(args[0]);
            if(args.length == 2) {
                try{
                    numberOfThreads = Integer.parseInt(args[1]);
                }catch (Exception e) {
                    System.err.println("User run error: please enter a number greater than 0");
                    return;
                }
            }
            if(numberOfThreads <= 0){
                System.err.println("User run error: please enter a number greater than 0");
                return;
            }
            fileName = args[0].replaceAll(".list", "");
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            sizeOfFile = sizeOfFile(listOfURL.get(0));
            if(sizeOfFile <= 0) {
                System.err.println("The file size is unknown or zero");
                return;
            }
        } catch (MalformedURLException e) {
            System.err.println("The URL address is invalid");
            return;
        } catch (IOException e) {
            System.err.println("There is problem with size of the file or with list of the URL");
            return;
        }
        ManagerDownloader md = new ManagerDownloader();
        md.manager(listOfURL, fileName, sizeOfFile, numberOfThreads);
        System.out.println("Download succeeded");
    }

    /**
     *  Return list of all the URL that exist in the file
     *  the file is containing a mirrors of URL to the same download file
     * @param nameOfListFile
     * @return list of all the URL that exist in the file
     */
    public static List<String> listOfURL(String nameOfListFile){
        List<String> listOfURL = new ArrayList<>();
        File file = new File(nameOfListFile);
        if(file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(nameOfListFile));
                String line;
                while ((line = reader.readLine()) != null) listOfURL.add(line); // Adding all the URL address

            } catch (FileNotFoundException e) {
                System.err.println("The file not found");
            } catch (IOException e) {
                System.err.println("can not read from the URL file");
            }
        } else listOfURL.add(nameOfListFile); // The address is a URL
        return listOfURL;
    }

    /**
     *  Return the size of the file from the URL
     * @param urlPath
     * @return
     * @throws IOException
     */
    public static long sizeOfFile(String urlPath) throws IOException {

        URL url = new URL(urlPath);
        HttpURLConnection connLength = (HttpURLConnection) url.openConnection();
        connLength.setRequestMethod("HEAD");
        connLength.getInputStream();
        return connLength.getContentLength(); // Get the size of the file
    }
}
