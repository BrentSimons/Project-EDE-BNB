package fact.it.bnbservice.repository;

import fact.it.bnbservice.model.Bnb;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface BnbRepository extends JpaRepository<Bnb, Long> {
    List<Bnb> getBnbsByNameContains(String str);
}
