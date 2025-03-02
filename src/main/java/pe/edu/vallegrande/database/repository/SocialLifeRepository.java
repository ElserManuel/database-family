package pe.edu.vallegrande.database.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.database.model.SocialLife;

public interface SocialLifeRepository extends ReactiveCrudRepository<SocialLife, Integer> {
    
}
