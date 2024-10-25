package in.ua.icetools.icedata.dto;

import in.ua.icetools.icedata.models.Supplier;
import lombok.Data;

@Data
public class SupplierNamesDTO {

    private final String supplierName;

    public SupplierNamesDTO(Supplier supplier) {
        supplierName = supplier.getSupplierName();
    }

}
