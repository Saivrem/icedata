package in.ua.icetools.icedata.exceptions;

public class InvalidXmlTagStructureException extends Exception {

    private final String line;
    private final String cause;

    public InvalidXmlTagStructureException(String line, String cause) {
        this.line = line;
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "InvalidXmlTagStructureException {\n" +
                "\tline='" + line + '\'' + ",\n" +
                "\tcause='" + cause + '\'' + "\n" +
                '}';
    }
}
