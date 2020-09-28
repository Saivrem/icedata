package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.resources.SupplierRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static in.ua.icetools.icedata.processors.Utils.*;

/**
 * Processor for SuppliersList.xml file.
 */
public class SupplierProcessor {

    private final SupplierRepository supplierRepository;

    public SupplierProcessor(SupplierRepository supplierRepository, String userName, String passWord) {
        this.supplierRepository = supplierRepository;
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord.toCharArray());
            }
        });
    }

    /**
     * The only meaningful method for any Processor, probably I should make them an interface in the future.
     *
     * @param url URL object with the file (TODO move to CONSTANTS class);
     * @return String with the response, Either everything went good or not
     * @throws Exception One of many possible exceptions (TODO Think about it)
     */
    public String process(URL url) throws Exception {
        //TODO rework path1 and path2 to be temporary files in system native TMP dir
        File suppliersFile = new File("path1");

        downloadURL(url, suppliersFile);

        File resultFile = new File("path2");

        unGzip(suppliersFile, resultFile);

        BufferedReader reader = new BufferedReader(new FileReader(resultFile));
        int counter = 0;
        List<Supplier> supplierList = new ArrayList<>();
        while (reader.ready()) {
            String line = reader.readLine();
            line = line.trim();
            if (line.startsWith("<Supplier ")) {
                LinkedHashMap<String, String> attributes = readAttribute(line);
                Supplier supplier = new Supplier();
                supplier.setBrandLogo(attributes.get("LogoOriginal") != null ? attributes.get("LogoOriginal") : "");
                supplier.setIsSponsor(attributes.get("Sponsor") != null ? Integer.parseInt(attributes.get("Sponsor")) : 0);
                supplier.setSupplierId(Long.parseLong(attributes.get("ID")));
                supplier.setSupplierName(attributes.get("Name"));
                supplierList.add(supplier);

                counter++;

                if (counter % 10 == 0) {
                    System.out.printf("\rRead %d suppliers", counter);
                }
            }
        }

        System.out.printf("\rRead %d suppliers\n", counter);

        resultFile.delete();
        suppliersFile.delete();

        supplierRepository.saveAll(supplierList);

        //This could be actually a lie, think how to provide valid data.
        return String.format("%d suppliers saved to DB", counter);

    }


}
