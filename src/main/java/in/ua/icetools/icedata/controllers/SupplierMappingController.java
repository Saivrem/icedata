package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.processors.SupplierMappingProcessor;
import in.ua.icetools.icedata.resources.SupplierMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URL;
import java.util.Properties;

@RestController
@RequestMapping("api/v1/supplierMappings")
public class SupplierMappingController {

    @Autowired
    SupplierMappingRepository supplierMappingRepository;

    @PostMapping("/init")
    public ResponseEntity<String> initSupplierMappings(@Valid @RequestBody Properties properties) {
        String response = "Something went wrong";
        try {
            SupplierMappingProcessor processor = new SupplierMappingProcessor(
                    supplierMappingRepository,
                    properties.getProperty("userName"),
                    properties.getProperty("passWord")
            );
            response = processor.process(new URL(properties.getProperty("url")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(response);
    }
}
