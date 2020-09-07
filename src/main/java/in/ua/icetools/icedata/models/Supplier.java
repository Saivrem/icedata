package in.ua.icetools.icedata.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @Column(name = "supplier_id", unique = true, nullable = false)
    private Long supplier_id;
    @Column(name = "supplier_name", unique = false, nullable = false)
    private String supplier_name;
    @Column(name = "is_sponsor", unique = false, nullable = false)
    private int is_sponsor;
    @Column(name = "brand_logo", unique = false, nullable = false)
    private String brand_logo;

    public Supplier() {
    }

    public Supplier(Long supplier_id, String supplier_name, int is_sponsor, String brand_logo) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.is_sponsor = is_sponsor;
        this.brand_logo = brand_logo;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getIs_sponsor() {
        return is_sponsor;
    }

    public void setIs_sponsor(int is_sponsor) {
        this.is_sponsor = is_sponsor;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }


}
