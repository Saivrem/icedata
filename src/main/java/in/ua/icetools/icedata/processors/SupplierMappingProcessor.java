package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.SupplierMapping;
import in.ua.icetools.icedata.resources.SupplierMappingRepository;

import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import static in.ua.icetools.icedata.processors.Utils.readAttribute;

public class SupplierMappingProcessor {

    private final SupplierMappingRepository repository;

    public SupplierMappingProcessor(SupplierMappingRepository repository, String userName, String passWord) {
        this.repository = repository;
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord.toCharArray());
            }
        });
    }

    public String process(URL url) throws Exception {
        System.out.println("start " + new Date().toString());
        repository.truncate();

        //url.openConnection();
        File suppliersFile = new File("suppliersFile");
        int totalCounter = 0;
        try (InputStream stream = url.openStream()) {
            Files.copy(stream, suppliersFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(suppliersFile))) {
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

                if (counter == 3000 || !reader.ready()) {
                    repository.saveAll(mappingsList);
                    mappingsList = new ArrayList<>();
                    counter = 0;
                    System.out.printf("\rNumber of processed supplier mappings: %d", totalCounter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        suppliersFile.delete();
        System.out.println("\nend " + new Date().toString());
        return String.format("%d supplier mappings saved to repository", totalCounter);
    }
}
