package in.ua.icetools.icedata.dto;

import in.ua.icetools.icedata.models.Supplier;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SupplierDTO {

    private final long supplierId;
    private final String supplierName;
    private final boolean isSponsor;
    private final String brandLogo;
    private final List<String> brandMappings = new ArrayList<>();

    public SupplierDTO(Supplier supplier, boolean withMappings) {
        supplierId = supplier.getSupplierId();
        supplierName = supplier.getSupplierName();
        brandLogo = supplier.getBrandLogo();
        isSponsor = supplier.getIsSponsor() == 1;
        if (withMappings) {
            supplier.getSupplierMappingList().forEach(supplierMapping -> {
                brandMappings.add(supplierMapping.getMSupplierName());
            });
        }
    }
}
