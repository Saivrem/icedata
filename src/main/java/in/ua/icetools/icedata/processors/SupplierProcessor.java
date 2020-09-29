package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.resources.SupplierRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static in.ua.icetools.icedata.constants.RepositoryLinks.SUPPLIERS_LIST_URL;
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
     * @return String with the response, Either everything went good or not
     */
    public String process() throws Exception {
        //TODO rework path1 and path2 to be temporary files in system native TMP dir
        File suppliersFile = new File("gzippedSuppliersFile.tmp");
        File resultFile = new File("unzippedSuppliersFile.tmp");
        suppliersFile.deleteOnExit();
        resultFile.deleteOnExit();

        downloadURL(SUPPLIERS_LIST_URL, suppliersFile);
        unGzip(suppliersFile, resultFile);

        int suppliersReadCounter = 0;
        List<Supplier> supplierList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {

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

                    suppliersReadCounter++;

                    if (suppliersReadCounter == 3000 || !reader.ready()) {
                        supplierRepository.saveAll(supplierList);
                        System.out.printf("\rRead %d suppliers", suppliersReadCounter);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("\rRead %d suppliers\n", suppliersReadCounter);

        //This could be actually a lie, think how to provide valid data.
        return String.format("%d suppliers saved to DB", suppliersReadCounter);

    }


}
