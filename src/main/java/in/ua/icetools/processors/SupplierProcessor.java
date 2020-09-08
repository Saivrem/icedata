package in.ua.icetools.processors;

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
                supplier.setBrand_logo(attributes.get("LogoOriginal") != null ? attributes.get("LogoOriginal") : "");
                supplier.setIs_sponsor(attributes.get("Sponsor") != null ? Integer.parseInt(attributes.get("Sponsor")) : 0);
                supplier.setSupplier_id(Long.parseLong(attributes.get("ID")));
                supplier.setSupplier_name(attributes.get("Name"));
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
