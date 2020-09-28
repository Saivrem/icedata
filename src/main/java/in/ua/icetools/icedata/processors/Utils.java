package in.ua.icetools.icedata.processors;

import java.util.LinkedHashMap;

public class Utils {
    public static LinkedHashMap<String, String> readAttribute(String line) {

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
                    if (array[i] != '=') {
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
                        attributes.put(name.toString().trim(), value.toString().replaceAll("\"", "").trim());
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
}
