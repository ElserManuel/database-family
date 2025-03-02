package pe.edu.vallegrande.database.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.database.model.Person;
import reactor.core.publisher.Flux;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, Integer> {
    
    @Query("SELECT * FROM person WHERE state = :state")
    Flux<Person> findByState(String state);

    @Query("SELECT * FROM person WHERE family_id_family = :familyId")
    Flux<Person> findByFamilyId(Integer familyId);

    @Query("SELECT * FROM person WHERE family_id_family IS NULL")
    Flux<Person> findPersonsWithNullFamilyId();

}
