package in.ua.icetools.icedata.controllers.v1;

import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.processors.v1.OldLanguageProcessor;
import in.ua.icetools.icedata.processors.v1.SupplierMappingProcessor;
import in.ua.icetools.icedata.processors.v1.SupplierProcessor;
import in.ua.icetools.icedata.repositories.v1.OldLanguageNameRepository;
import in.ua.icetools.icedata.repositories.v1.OldLanguageRepository;
import in.ua.icetools.icedata.repositories.v1.SupplierMappingRepository;
import in.ua.icetools.icedata.repositories.v1.SupplierRepository;
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
    @Autowired
    OldLanguageRepository oldLanguageRepository;
    @Autowired
    OldLanguageNameRepository oldLanguageNameRepository;

    /**
     * Method to init all tables implemented
     *
     * @param properties receives username and password in properties object
     * @return String value "Done" for good and error message for other options
     */
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
            oldLanguageRepository.saveAll(OldLanguageProcessor.process(false, null));
            oldLanguageNameRepository.saveAll(OldLanguageProcessor.getNamesList());
        } catch (Exception e) {
            response = e.getMessage();
        }
        return ResponseEntity.ok().body(response);

    }

}
