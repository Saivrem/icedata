package in.ua.icetools.icedata.repositories.v1;

import in.ua.icetools.icedata.models.v1.OldLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldLanguageRepository extends JpaRepository<OldLanguage, Long> {
}
