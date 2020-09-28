package in.ua.icetools.icedata.models;

import javax.persistence.*;

@Entity
@Table(name = "supplier_mappings")
public class SupplierMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", unique = true)
    private long recordId;

    @Column(name = "supplier_id")
    private long supplierId;

    @Column(name = "m_supplier_name")
    private String mSupplierName;

    public SupplierMapping() {
    }

    public SupplierMapping(long supplierId, String mSupplierName) {
        this.supplierId = supplierId;
        this.mSupplierName = mSupplierName;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getMSupplierName() {
        return mSupplierName;
    }

    public void setMSupplierName(String mSupplierName) {
        this.mSupplierName = mSupplierName;
    }
}


