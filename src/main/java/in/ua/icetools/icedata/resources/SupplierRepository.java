package in.ua.icetools.icedata.resources;

import in.ua.icetools.icedata.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
