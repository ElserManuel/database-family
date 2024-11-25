package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.HousingEnvironment;

public interface HousingEnvironmentRepository extends ReactiveCrudRepository<HousingEnvironment, Integer> {
    
}