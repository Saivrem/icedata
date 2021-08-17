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
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

/**
 * Helper class with static methods
 */
public class Utils {

    public static void unGzip(Path inputFile, Path resultFile) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputFile.toFile()))) {
            Files.copy(gzipInputStream, resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * V2 version of Download URL
     *
     * @param link String for URL
     * @param path Path to result file.
     * @throws IOException Exception is thrown upper
     */
    public static void downloadUrl(String link, Path path) throws IOException {
        InputStream stream = new URL(link).openStream();
        Files.copy(stream, path);
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

    /**
     * Unpacks GZ file;
     *
     * @param inputFile  input (gzipped) file;
     * @param resultFile result file;
     */
    @Deprecated
    public static void oldUnGzip(File inputFile, File resultFile) {
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
    @Deprecated
    public static void oldDownloadURL(String link, File outputFile) throws MalformedURLException {
        URL url = new URL(link);
        try (InputStream stream = url.openStream()) {
            Files.copy(stream, outputFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to read attributes from XML tag line
     *
     * @param line input line with XML tag
     * @return LinkedHashMap with attributes in the same order they've been read from the string
     */
    @Deprecated
    public static LinkedHashMap<String, String> readAttribute(String line) throws InvalidXmlTagStructureException {

        char[] array = line.toCharArray();
        LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
        StringBuilder tagName = new StringBuilder();
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isName = true;
        boolean opening = true;
        boolean isValue = false;
        for (int i = 0; i < array.length; i++) {
            if (opening) {
                opening = !(array[i] == ' ');
                tagName.append(array[i]);
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
                        attributes.put(name.toString().trim(), value.toString().trim());
                        name = new StringBuilder();
                        value = new StringBuilder();
                    } else {
                        value.append(array[i]);
                    }
                }
            }
        }
        attributes.put("tag", tagName.toString().trim().replaceAll("<", ""));
        return attributes;
    }
}
