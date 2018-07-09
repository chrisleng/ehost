package verifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileString {

    /**
     * Takes in a file name and will turn the entire file line by line into a
     * long connected string.
     *
     * @param filename - The name of the file that you want to be read in
     * @throws IOException - The exception will be thrown if you give it an
     * invalid filename
     * @return String Result - This will return the file you read in as a single
     * long string
     *
     * @author Timothy Ryan Beavers @date March 15th 2010
     *
     */
    public static String readFile(String fileName) throws IOException {

        BufferedReader readIn = new BufferedReader(new FileReader(fileName)); //create the reader
        String result = "";
        String input = "";

        //read file into a string
        while ((input = readIn.readLine()) != null) {
            result = result + input + "\n"; //take each line and put it into a new string
        }

        return result; // return the file as a long string

    }
}
