package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.processors.SupplierMappingProcessor;
import in.ua.icetools.icedata.processors.SupplierProcessor;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.resources.SupplierMappingRepository;
import in.ua.icetools.icedata.resources.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Properties;

@Controller
@RequestMapping("api/v1/general")
public class GeneralController {

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierMappingRepository supplierMappingRepository;

    @PostMapping("/init")
    public ResponseEntity<String> init(@Valid @RequestBody Properties properties) {

        String response = "Done";

        String userName = properties.getProperty("userName");
        String passWord = properties.getProperty("passWord");

        Utils.authenticate(userName, passWord);
        try {
            supplierRepository.saveAll(SupplierProcessor.process(false, null));
            supplierMappingRepository.truncate();
            supplierMappingRepository.saveAll(SupplierMappingProcessor.process());
        } catch (Exception e) {
            response = e.getMessage();
        }
        return ResponseEntity.ok().body(response);

    }

}
