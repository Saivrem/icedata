package in.ua.icetools.icedata.models;

import javax.persistence.*;

@Entity
@Table(name = "supplier_mappings", indexes = {
        @Index(name = "supplier_id", columnList = "supplier_id"),
        @Index(name = "m_supplier_name", columnList = "m_supplier_name")
})
public class SupplierMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", unique = true)
    private long recordId;

    @Column(name = "supplier_id")
    private long supplierId;

    @Column(name = "m_supplier_name")
    private String mappedSupplierName;

    public SupplierMapping() {
    }

    public SupplierMapping(long supplierId, String mappedSupplierName) {
        this.supplierId = supplierId;
        this.mappedSupplierName = mappedSupplierName;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getMSupplierName() {
        return mappedSupplierName;
    }

    public void setMSupplierName(String mSupplierName) {
        this.mappedSupplierName = mSupplierName;
    }

}


