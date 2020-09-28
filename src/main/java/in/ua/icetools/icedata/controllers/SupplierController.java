package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.exceptions.ResourceNotFoundException;
import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.processors.SupplierProcessor;
import in.ua.icetools.icedata.resources.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("api/v1/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("/all")
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @GetMapping("/id={id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable(value = "id") Long supplier_id) throws ResourceNotFoundException {
        Supplier supplier = supplierRepository.findById(supplier_id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found for this id :: " + supplier_id));
        return ResponseEntity.ok().body(supplier);
    }

    @GetMapping("/name={name}")
    public ResponseEntity<List<Supplier>> getSupplierByName(@PathVariable(value = "name") String supplierName) {
        List<Supplier> suppliers = supplierRepository.findSupplierByName(supplierName);
        return ResponseEntity.ok().body(suppliers);
    }

    @GetMapping("/ids={string}")
    public ResponseEntity<List<Supplier>> getSuppliersInBatch(@PathVariable(value = "string") String line) {
        List<Long> ids = new ArrayList<>();
        for (String str : line.split(",")) {
            ids.add(Long.parseLong(str));
        }
        List<Supplier> resultList = supplierRepository.findAllById(ids);
        return ResponseEntity.ok().body(resultList);
    }

    @PostMapping("/")
    public ResponseEntity<List<Supplier>> getSuppliersById(@Valid @RequestBody List<Long> ids) {
        List<Supplier> resultList = supplierRepository.findAllById(ids);
        return ResponseEntity.ok().body(resultList);
    }

    @PostMapping("/init")
    public ResponseEntity<String> initSuppliers(@Valid @RequestBody Properties properties) {

        String response = "Something went wrong";
        try {
            SupplierProcessor processor = new SupplierProcessor(
                    supplierRepository,
                    properties.getProperty("userName"),
                    properties.getProperty("passWord")
            );
            response = processor.process(new URL(properties.getProperty("url")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(response);
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
