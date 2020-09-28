package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.processors.SupplierMappingProcessor;
import in.ua.icetools.icedata.processors.SupplierProcessor;
import in.ua.icetools.icedata.resources.SupplierMappingRepository;
import in.ua.icetools.icedata.resources.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URL;
import java.util.Properties;

@Controller
@RequestMapping("api/v1/general")
public class GeneralController {

    private final static String suppliersListUrl = "https://data.icecat.biz/export/freexml/refs/SuppliersList.xml.gz";
    private final static String supplierMappingUrl = "https://data.icecat.biz/export/level4/EN/supplier_mapping.xml";
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierMappingRepository supplierMappingRepository;

    @PostMapping("/init")
    public ResponseEntity<String> init(@Valid @RequestBody Properties properties) {

        String response = "Something went wrong";

        String userName = properties.getProperty("userName");
        String passWord = properties.getProperty("passWord");

        SupplierProcessor supplierProcessor = new SupplierProcessor(
                supplierRepository,
                userName,
                passWord
        );
        SupplierMappingProcessor supplierMappingProcessor = new SupplierMappingProcessor(supplierMappingRepository,
                userName,
                passWord
        );

        try {
            response = String.format("%s\n%s",
                    supplierProcessor.process(new URL(suppliersListUrl)),
                    supplierMappingProcessor.process(new URL(supplierMappingUrl)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(response);

    }

}
