package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.exceptions.InvalidXmlTagStructureException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UtilsTest {

    private static final HashMap<String, String> testCases = new HashMap<>();
    private static final LinkedHashMap<String, String> expectedMap = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> expectedMapForException = new LinkedHashMap<>();

    static {
        testCases.put("positive", "<Tag attr1=\"simple\" attr2=\"with spaces within\" attr3=\"with ;amp&\">");
        testCases.put("exception", "<Tag attr1=\"simple\" attr2=\"with\" quotes within\" attr3=\"with ;amp&\">");

        expectedMap.put("attr1", "simple");
        expectedMap.put("attr2", "with spaces within");
        expectedMap.put("attr3", "with ;amp&");
        expectedMap.put("tag", "Tag");

        expectedMapForException.put("attr1", "simple");
        expectedMapForException.put("attr2", "with");
        expectedMapForException.put("quotes within\" attr3", "with ;amp&");
    }

    @Test()
    void readAttributeException() {
        try {
            assertEquals(expectedMapForException, Utils.readAttribute(testCases.get("exception")));
            fail("InvalidXmlTagStructureException expected");
        } catch (InvalidXmlTagStructureException ex) {
            System.out.println("Exception thrown successfully");
            System.out.println(ex.toString());
        }
    }

    @Test
    void readAttributeSuccess() {
        try {
            assertEquals(expectedMap, Utils.readAttribute(testCases.get("positive")));
            System.out.println("Positive case successful");
        } catch (InvalidXmlTagStructureException ex) {
            //This should not happen here
            ex.printStackTrace();
        }
    }

}