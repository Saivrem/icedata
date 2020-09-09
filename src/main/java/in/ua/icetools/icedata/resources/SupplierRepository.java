package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query(value = "SELECT * FROM suppliers s WHERE s.supplier_name = :name", nativeQuery = true)
    List<Supplier> findSupplierByName(@Param("name") String name);
}
