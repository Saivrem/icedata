package in.ua.icetools.icedata.dto;

import in.ua.icetools.icedata.models.Supplier;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SupplierDTO {

    @Getter
    private final long supplierId;
    @Getter
    private final String supplierName;
    @Getter
    private final boolean isSponsor;
    @Getter
    private final String brandLogo;
    @Getter
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

    @Override
    public String toString() {
        return "SupplierDTO{" +
                "supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", isSponsor=" + isSponsor +
                ", brandLogo='" + brandLogo + '\'' +
                ", brandMappings=" + brandMappings +
                '}';
    }
}
