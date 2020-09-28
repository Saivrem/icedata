package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.SupplierMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SupplierMappingRepository extends JpaRepository<SupplierMapping, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE supplier_mappings", nativeQuery = true)
    void truncate();
}
