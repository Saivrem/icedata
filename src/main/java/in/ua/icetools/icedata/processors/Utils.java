package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.exceptions.InvalidXmlTagStructureException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

/**
 * Helper class with static methods
 */
public class Utils {

    /**
     * Helper method to read attributes from XML tag line
     *
     * @param line input line with XML tag
     * @return LinkedHashMap with attributes in the same order they've been read from the string
     */
    public static LinkedHashMap<String, String> readAttribute(String line) throws InvalidXmlTagStructureException {

        char[] array = line.toCharArray();
        LinkedHashMap<String, String> attributes = new LinkedHashMap<>();

        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isName = true;
        boolean opening = true;
        boolean isValue = false;
        for (int i = 0; i < array.length; i++) {
            if (opening) {
                opening = !(array[i] == ' ');
            } else {
                if (isName) {
                    if (array[i] == '\"') {
                        throw new InvalidXmlTagStructureException(line, "Structure is broken");
                    } else if (array[i] != '=') {
                        name.append(array[i]);
                    } else {
                        isName = false;
                    }
                } else {
                    if ((array[i] == '\"') && !isValue) {
                        isValue = true;
                    } else if ((array[i] == '\"') && isValue) {
                        isName = true;
                        isValue = false;
                        attributes.put(name.toString().trim(), value.toString().trim()); //.replaceAll("\"", "").trim());
                        name = new StringBuilder();
                        value = new StringBuilder();
                    } else {
                        value.append(array[i]);
                    }
                }
            }
        }
        return attributes;
    }

    /**
     * Unpacks GZ file;
     *
     * @param inputFile  input (gzipped) file;
     * @param resultFile result file;
     */
    public static void unGzip(File inputFile, File resultFile) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputFile))) {
            Files.copy(gzipInputStream, resultFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads URL contents
     *
     * @param link       String URL
     * @param outputFile output file
     */
    public static void downloadURL(String link, File outputFile) throws MalformedURLException {
        URL url = new URL(link);
        try (InputStream stream = url.openStream()) {
            Files.copy(stream, outputFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Auth method
     *
     * @param userName username
     * @param passWord password
     */
    public static void authenticate(String userName, String passWord) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord.toCharArray());
            }
        });
    }
}
