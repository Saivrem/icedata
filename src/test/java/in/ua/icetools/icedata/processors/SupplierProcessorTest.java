package in.ua.icetools.icedata.processors;

import in.ua.icetools.icedata.models.Supplier;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplierProcessorTest {

    private static final List<Supplier> expectedList = new ArrayList<>();

    static {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(1L);
        supplier.setSupplierName("HP");
        supplier.setIsSponsor(1);
        supplier.setBrandLogo("http://images.icecat.biz/img/brand/original/1_e8b5a9ede56c45cf9428740dc937d071.png");
        expectedList.add(supplier);
    }

    private static File prepareFile() {

        String toTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE ICECAT-interface SYSTEM \"https://data.icecat.biz/dtd/ICECAT-interface_response.dtd\">\n" +
                "<!--source: Icecat.biz 2020-->\n" +
                "<ICECAT-interface xmlns:xsi=\"https://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"https://data.icecat.biz/xsd/ICECAT-interface_response.xsd\">\n" +
                "<Response Date=\"Tue Oct 20 15:00:02 2020\" ID=\"0\" Request_ID=\"1603198802\" Status=\"1\">\n" +
                "<SuppliersList>\n" +
                "<Supplier ID=\"1\" LogoPic=\"http://images.icecat.biz/img/brand/thumb/1_cf8603f6de7b4c4d8ac4f5f0ef439a05.jpg\" LogoPicHeight=\"75\" LogoPicWidth=\"75\" LogoPicSize=\"9425\" LogoLowPic=\"http://images.icecat.biz/img/brand/low/1_d0a20d639fc74672915c2b5e13682a06.jpg\" LogoLowPicHeight=\"200\" LogoLowPicWidth=\"195\" LogoLowSize=\"30145\" LogoMediumPic=\"http://images.icecat.biz/img/brand/medium/1_0aec37306b8647b2a6043657e5749396.jpg\" LogoMediumPicHeight=\"500\" LogoMediumPicWidth=\"487\" LogoMediumPicSize=\"83864\" LogoHighPic=\"http://images.icecat.biz/img/brand/high/1_df42767c3ddf465b89464fb59064c6ce.jpg\" LogoHighPicHeight=\"642\" LogoHighPicWidth=\"625\" LogoHighPicSize=\"113825\" LogoOriginal=\"http://images.icecat.biz/img/brand/original/1_e8b5a9ede56c45cf9428740dc937d071.png\" LogoOriginalSize=\"119902\" Name=\"HP\" Sponsor=\"1\">\n" +
                "<Names>\n" +
                "<Name langid=\"12\" Name=\"惠普\"/>\n" +
                "</Names>\n" +
                "</Supplier>\n" +
                "</SuppliersList>\n" +
                "</Response>\n" +
                "</ICECAT-interface>";

        try {
            File tempFile = File.createTempFile("testCase", ".tmp");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            tempFile.deleteOnExit();
            writer.write(toTest);
            writer.flush();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    void process() {
        try {
            File tempFile = prepareFile();
            List<Supplier> testExample = tempFile != null ? SupplierProcessor.process(true, tempFile) : new ArrayList<>();
            assertEquals(expectedList, testExample);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}