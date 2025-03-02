package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.HousingDistribution;

public interface HousingDistributionRepository extends ReactiveCrudRepository<HousingDistribution, Integer> {
    
}