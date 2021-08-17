package in.ua.icetools.icedata.controllers.v1;

import in.ua.icetools.icedata.dto.SupplierDTO;
import in.ua.icetools.icedata.dto.SupplierNamesDTO;
import in.ua.icetools.icedata.models.v1.Supplier;
import in.ua.icetools.icedata.processors.Utils;
import in.ua.icetools.icedata.processors.v1.SupplierProcessor;
import in.ua.icetools.icedata.repositories.v1.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("api/v1/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Important!!! This method returns suppliers without mappings
     *
     * @return List of all suppliers without Mappings
     */
    @GetMapping("/all")
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> allSuppliers = new ArrayList<>();
        for (Supplier supplier : supplierRepository.findAll()) {
            allSuppliers.add(new SupplierDTO(supplier, false));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok().headers(headers).body(allSuppliers);
    }

    @GetMapping("/allNames")
    public ResponseEntity<List<SupplierNamesDTO>> getAllSupplierNames() {
        List<SupplierNamesDTO> allSuppliers = new ArrayList<>();
        for (Supplier supplier : supplierRepository.findAll()) {
            allSuppliers.add(new SupplierNamesDTO(supplier));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok().headers(headers).body(allSuppliers);
    }

    @GetMapping("/id")
    @ResponseBody //todo check why I've put this here.
    public ResponseEntity<List<SupplierDTO>> getSuppliersInBatch(@RequestParam List<Long> id) {
        List<SupplierDTO> resultList = new ArrayList<>();
        for (Supplier supplier : supplierRepository.findAllById(id)) {
            resultList.add(new SupplierDTO(supplier, false));
        }
        return ResponseEntity.ok().body(resultList);
    }

    @GetMapping("/getByNames={name}")
    public ResponseEntity<List<SupplierDTO>> getSupplierByName(@PathVariable(value = "name") String supplierName) {
        List<SupplierDTO> result = new ArrayList<>();
        for (Supplier supplier : supplierRepository.findSupplierByName(Arrays.asList(supplierName.split(",")))) {
            result.add(new SupplierDTO(supplier, false));
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/getByMName={name}")
    public ResponseEntity<Supplier> getSupplierByMappedName(@PathVariable(value = "name") String mName) {
        Supplier s = supplierRepository.getSupplierByMappedName(mName);
        return ResponseEntity.ok().body(s);
    }

    @PostMapping("/")
    public ResponseEntity<List<Supplier>> getSuppliersById(@Valid @RequestBody List<Long> ids) {
        List<Supplier> resultList = supplierRepository.findAllById(ids);
        return ResponseEntity.ok().body(resultList);
    }

    @PostMapping("/init")
    public ResponseEntity<String> initSuppliers(@Valid @RequestBody Properties properties) {
        String response = "Done";
        try {
            Utils.authenticate(properties.getProperty("userName"), properties.getProperty("passWord"));
            supplierRepository.saveAll(SupplierProcessor.process(false, null));
        } catch (Exception e) {
            response = e.getMessage();
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/getByNames")
    public ResponseEntity<List<SupplierDTO>> getSuppliersByNamesWithMappings(@Valid @RequestBody Properties properties) {
        String[] names = properties.getProperty("names").split(",");
        boolean withMappings = Boolean.parseBoolean(properties.getProperty("mappings"));

        List<Supplier> suppliers = supplierRepository.findSupplierByName(Arrays.asList(names));
        List<SupplierDTO> result = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            result.add(new SupplierDTO(supplier, withMappings));
        }

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/")
    public Supplier createSupplier(@Valid @RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @DeleteMapping("/id={id}")
    public void deleteById(@PathVariable(value = "id") Long supplier_id) {
        supplierRepository.deleteById(supplier_id);
    }
}
