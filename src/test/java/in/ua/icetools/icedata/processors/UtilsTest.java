package in.ua.icetools.icedata.processors;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void readAttribute() {

        String toTest = "<Tag attr1=\"One\" attr2=\"Two\">";
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();

        resultMap.put("attr1", "One");
        resultMap.put("attr2", "Two");

        assertEquals(resultMap, Utils.readAttribute(toTest));
        System.out.println("Done");

    }
}