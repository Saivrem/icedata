package in.ua.icetools.icedata.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "suppliers")
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

    public Supplier() {
    }

    public Supplier(Long supplierId, String supplierName, int isSponsor, String brandLogo) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.isSponsor = isSponsor;
        this.brandLogo = brandLogo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplier_name) {
        this.supplierName = supplier_name;
    }

    public int getIsSponsor() {
        return isSponsor;
    }

    public void setIsSponsor(int is_sponsor) {
        this.isSponsor = is_sponsor;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brand_logo) {
        this.brandLogo = brand_logo;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplier_id) {
        this.supplierId = supplier_id;
    }

    public List<SupplierMapping> getSupplierMappingList() {
        return supplierMappingList;
    }

    public void setSupplierMappingList(List<SupplierMapping> supplierMappingList) {
        this.supplierMappingList = supplierMappingList;
    }
}
