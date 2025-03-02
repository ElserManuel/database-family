package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.FamilyHealth;

public interface FamilyHealthRepository extends ReactiveCrudRepository<FamilyHealth, Integer> {
    
}
