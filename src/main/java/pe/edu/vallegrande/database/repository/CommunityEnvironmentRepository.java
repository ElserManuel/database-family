package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.database.model.CommunityEnvironment;

@Repository
public interface CommunityEnvironmentRepository extends ReactiveCrudRepository<CommunityEnvironment, Integer> {
     
}