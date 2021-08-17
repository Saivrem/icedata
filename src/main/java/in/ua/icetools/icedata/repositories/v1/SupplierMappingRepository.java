package in.ua.icetools.icedata.repositories.v1;

import in.ua.icetools.icedata.models.v1.SupplierMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SupplierMappingRepository extends JpaRepository<SupplierMapping, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE `supplier_mappings`", nativeQuery = true)
    void truncate();

    @Query(value = "select count(*) from `supplier_mappings`", nativeQuery = true)
    long count();

    @Query(value = "select * from `supplier_mappings` where m_supplier_name= :name", nativeQuery = true)
    SupplierMapping getByMName(@Param("name") String name);

}
