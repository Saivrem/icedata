package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(value = "select s from Supplier s where s.supplierName in :name")
    List<Supplier> findSupplierByName(@Param("name") Iterable<String> name);

}
