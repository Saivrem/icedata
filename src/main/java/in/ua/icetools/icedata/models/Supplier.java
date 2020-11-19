package in.ua.icetools.icedata.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "suppliers", indexes = {
        @Index(name = "supplier_id", columnList = "supplier_id", unique = true),
        @Index(name = "supplier_name", columnList = "supplier_name")
})
@Data
@NoArgsConstructor
public class Supplier {

    @Id
    @Column(name = "supplier_id", unique = true)
    private Long supplierId;
    @Column(name = "supplier_name")
    private String supplierName;
    @Column(name = "is_sponsor")
    private int isSponsor;
    @Column(name = "brand_logo")
    private String brandLogo;

    @OneToMany
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private List<SupplierMapping> supplierMappingList;

    public Supplier(Long supplierId, String supplierName, int isSponsor, String brandLogo) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.isSponsor = isSponsor;
        this.brandLogo = brandLogo;
    }
}
