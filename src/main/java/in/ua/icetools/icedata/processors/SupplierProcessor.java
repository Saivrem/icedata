package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.resources.SupplierRepository;

import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static in.ua.icetools.icedata.processors.Utils.readAttribute;

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

    public String process(URL url) throws Exception {

        url.openConnection();
        File suppliersFile = new File("path1");
        try (InputStream stream = url.openStream()) {
            Files.copy(stream, suppliersFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File resultFile = new File("path2");
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(suppliersFile))) {
            Files.copy(gzipInputStream, resultFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        return String.format("%d suppliers saved to DB", counter);

    }
}
