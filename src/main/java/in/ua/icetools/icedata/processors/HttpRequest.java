package in.ua.icetools.icedata.processors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Implementation of HttpRequest interface
 */
public class HttpRequest {

    /**
     * HTTP link to be requested;
     */
    private final URL url;

    /**
     * Constructor with URL object
     *
     * @param url URL object
     */
    public HttpRequest(URL url) {
        this.url = url;
    }

    /**
     * Makes Head request which return Map with headers
     *
     * @return Map, Key - String, List of strings;
     * @throws IOException may be thrown indeed;
     */
    public Map<String, List<String>> headRequest() throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("HEAD");
        return httpURLConnection.getHeaderFields();

    }

    /**
     * Filename Getter.
     *
     * @return String with the file name;
     */
    public String getFileName() {
        String tmp = url.getFile();
        return tmp.substring(tmp.lastIndexOf("/") + 1);
    }

    /**
     * Getter for original link.
     *
     * @return String with the link;
     */
    public String getOriginalLink() {
        return url.toString();
    }

}
