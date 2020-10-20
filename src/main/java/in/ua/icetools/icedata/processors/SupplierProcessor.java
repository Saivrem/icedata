package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;

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

    /**
     * The only meaningful method for any Processor, probably I should make them an interface in the future.
     *
     * @return String with the response, Either everything went good or not
     */
    public static List<Supplier> process(boolean test, File testFile) throws Exception {

        File resultFile = test ? testFile : new File("unzippedSuppliersFile.tmp");
        if (!test) {
            File suppliersFile = new File("gzippedSuppliersFile.tmp");

            downloadURL(SUPPLIERS_LIST_URL, suppliersFile);
            unGzip(suppliersFile, resultFile);
        }

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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supplierList;
    }


}
