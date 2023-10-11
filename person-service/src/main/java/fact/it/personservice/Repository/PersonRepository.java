package fact.it.personservice.Repository;

import fact.it.personservice.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findByFullNameIn(List<String> fullName);
}
