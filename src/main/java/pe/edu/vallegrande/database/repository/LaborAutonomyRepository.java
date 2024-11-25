package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.LaborAutonomy;

public interface LaborAutonomyRepository extends ReactiveCrudRepository<LaborAutonomy, Integer> {
    
}
