package in.ua.icetools.icedata.controllers;

import in.ua.icetools.icedata.exceptions.ResourceNotFoundException;
import in.ua.icetools.icedata.models.Supplier;
import in.ua.icetools.icedata.resources.SupplierRepository;
import in.ua.icetools.processors.SupplierProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @PostMapping("/suppliers")
    public Supplier createSupplier(@Valid @RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @GetMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable(value = "id") Long supplier_id) throws ResourceNotFoundException {
        Supplier supplier = supplierRepository.findById(supplier_id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + supplier_id));
        return ResponseEntity.ok().body(supplier);
    }

    @GetMapping("/suppliers")
    public List<Supplier> getAllUsers() {
        return supplierRepository.findAll();
    }

    /*@GetMapping("/suppliers/debug/{param}")
    public ResponseEntity<String> checkDebug(@PathVariable(value = "param") String debug) {
        if (debug.equals("1")) {
            return ResponseEntity.ok().body(SupplierProcessor.process());
        } else {
            return ResponseEntity.badRequest().body("Bad Request");
        }
    }*/

    @GetMapping("/suppliers/initSuppliers")
    public void initSuppliers() {
        try {
            SupplierProcessor processor = new SupplierProcessor(supplierRepository);
            processor.process(new URL("https://data.icecat.biz/export/freexml/refs/SuppliersList.xml.gz"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
