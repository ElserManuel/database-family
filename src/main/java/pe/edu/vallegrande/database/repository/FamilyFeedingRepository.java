package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.FamilyFeeding;

public interface FamilyFeedingRepository extends ReactiveCrudRepository<FamilyFeeding, Integer> {
    
}