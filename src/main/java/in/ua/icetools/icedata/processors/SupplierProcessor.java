package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.resources.SupplierRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static in.ua.icetools.icedata.constants.RepositoryLinks.SUPPLIERS_LIST_URL;
import static in.ua.icetools.icedata.processors.Utils.*;

/**
 * Processor for SuppliersList.xml file.
 */
public class SupplierProcessor {

    private final SupplierRepository repository;

    public SupplierProcessor(SupplierRepository repository, String userName, String passWord) {
        this.repository = repository;
        authenticate(userName, passWord);
    }

    /**
     * The only meaningful method for any Processor, probably I should make them an interface in the future.
     *
     * @return String with the response, Either everything went good or not
     */
    public String process() throws Exception {
        File suppliersFile = new File("gzippedSuppliersFile.tmp");
        File resultFile = new File("unzippedSuppliersFile.tmp");

        downloadURL(SUPPLIERS_LIST_URL, suppliersFile);
        unGzip(suppliersFile, resultFile);

        int suppliersReadCounter = 0;
        int totalCounter = 0;
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
                    totalCounter++;

                    if (suppliersReadCounter == 3000) {
                        repository.saveAll(supplierList);
                        supplierList = new ArrayList<>();
                        suppliersReadCounter = 0;
                        System.out.printf("\rNumber of processed suppliers: %d", totalCounter);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        repository.saveAll(supplierList);
        System.out.printf("\rNumber of processed suppliers: %d\n", totalCounter);

        System.out.printf("suppliersFile File removed: %s\n", suppliersFile.delete());
        System.out.printf("resultFile File removed: %s\n", resultFile.delete());
        return String.format("%d suppliers saved to DB", repository.count());

    }


}
