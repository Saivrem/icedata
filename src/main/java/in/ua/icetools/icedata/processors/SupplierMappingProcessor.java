package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.SupplierMapping;
import in.ua.icetools.icedata.resources.SupplierMappingRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static in.ua.icetools.icedata.constants.RepositoryLinks.SUPPLIER_MAPPING_URL;
import static in.ua.icetools.icedata.processors.Utils.*;

public class SupplierMappingProcessor {

    private final SupplierMappingRepository repository;

    public SupplierMappingProcessor(SupplierMappingRepository repository, String userName, String passWord) {
        this.repository = repository;
        authenticate(userName, passWord);
    }

    public String process() throws Exception {
        repository.truncate();

        File supplierMappingsFile = new File("supplierMappingsFile.tmp");
        System.out.printf("File removal status is: %s\n", supplierMappingsFile.delete());
        supplierMappingsFile.deleteOnExit();

        int totalCounter = 0;
        downloadURL(SUPPLIER_MAPPING_URL, supplierMappingsFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(supplierMappingsFile))) {
            boolean item = false;
            int supplierId = 0;
            int counter = 0;

            LinkedHashMap<String, String> list;
            ArrayList<SupplierMapping> mappingsList = new ArrayList<>();

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
                    counter++;
                    totalCounter++;
                }

                if (counter == 3000) {
                    repository.saveAll(mappingsList);
                    mappingsList = new ArrayList<>();
                    counter = 0;
                    System.out.printf("\rNumber of processed supplier mappings: %d", totalCounter);
                }

            }
            repository.saveAll(mappingsList);
            System.out.printf("\rNumber of processed supplier mappings: %d", totalCounter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("%d supplier mappings saved to repository", repository.count());
    }
}
