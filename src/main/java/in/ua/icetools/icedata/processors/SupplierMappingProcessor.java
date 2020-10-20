package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.SupplierMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static in.ua.icetools.icedata.constants.RepositoryLinks.SUPPLIER_MAPPING_URL;
import static in.ua.icetools.icedata.processors.Utils.downloadURL;
import static in.ua.icetools.icedata.processors.Utils.readAttribute;

public class SupplierMappingProcessor {

    public static List<SupplierMapping> process() throws Exception {

        File supplierMappingsFile = new File("supplierMappingsFile.tmp");

        downloadURL(SUPPLIER_MAPPING_URL, supplierMappingsFile);
        List<SupplierMapping> mappingsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(supplierMappingsFile))) {
            boolean item = false;
            int supplierId = 0;

            LinkedHashMap<String, String> list;


            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (line.contains("SupplierMapping ")) {
                    item = true;
                    list = readAttribute(line);
                    supplierId = Integer.parseInt(list.get("supplier_id"));
                    continue;
                } else if (line.equals("</SupplierMapping>")) {
                    item = false;
                }

                if (item) {
                    String name = line.replaceAll("^<Symbol(.*?)>", "");
                    name = name.replace("</Symbol>", "");
                    name = name.replaceAll("&amp;", "&");
                    name = name.toLowerCase();
                    mappingsList.add(new SupplierMapping(supplierId, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mappingsList;
    }
}
