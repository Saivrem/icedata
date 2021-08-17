package in.ua.icetools.icedata.repositories.v1;

import in.ua.icetools.icedata.models.v1.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(value = "select s from Supplier s where s.supplierName in :name")
    List<Supplier> findSupplierByName(@Param("name") Iterable<String> name);

    @Query(value = "select count(*) from `suppliers`", nativeQuery = true)
    long count();

    @Query(value = "select * from suppliers s join supplier_mappings sm on s.supplier_id = sm.supplier_id where sm.m_supplier_name= :mName", nativeQuery = true)
    Supplier getSupplierByMappedName(@Param("mName") String mName);
}
