package pe.edu.vallegrande.database.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.vallegrande.database.dto.PersonDTO;
import pe.edu.vallegrande.database.model.Person;
import pe.edu.vallegrande.database.repository.FamilyRepository;
import pe.edu.vallegrande.database.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class
PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FamilyRepository familyRepository;

    public Flux<PersonDTO> findAllActivePersonsWithFamily() {
        return personRepository.findAll()
            .filter(person -> "A".equals(person.getState())) // Filtrar por estado "A"
            .filter(person -> "PADRE".equals(person.getTypeKinship())) // Filtrar por estado "A"
            .flatMap(person -> { Integer familyId = person.getFamilyIdFamily();
                if (familyId == null) {
                    return Mono.empty();
                }
                return familyRepository.findById(familyId)
                    .map(family -> {
                        PersonDTO dto = new PersonDTO();
                        dto.setIdPerson(person.getIdPerson());
                        dto.setName(person.getName());
                        dto.setSurname(person.getSurname());
                        dto.setAge(person.getAge());
                        dto.setBirthdate(person.getBirthdate());
                        dto.setTypeDocument(person.getTypeDocument());
                        dto.setDocumentNumber(person.getDocumentNumber());
                        dto.setTypeKinship(person.getTypeKinship());
                        dto.setSponsored(person.getSponsored());
                        dto.setState(person.getState());
                        dto.setFamily(family);
                        return dto;
                    });
            });
    }

    private PersonDTO convertToDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setIdPerson(person.getIdPerson());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setAge(person.getAge());
        dto.setBirthdate(person.getBirthdate());
        dto.setTypeDocument(person.getTypeDocument());
        dto.setDocumentNumber(person.getDocumentNumber());
        dto.setTypeKinship(person.getTypeKinship());
        dto.setSponsored(person.getSponsored());
        dto.setState(person.getState());
        return dto;
    }

    public Flux<PersonDTO> findAllInactive() {
        return personRepository.findAll()
            .filter(person -> "I".equals(person.getState()))
            .flatMap(person -> { Integer familyId = person.getFamilyIdFamily();
                if (familyId == null) {
                    return Mono.empty();
                }
                return familyRepository.findById(familyId)
                    .map(family -> {
                        PersonDTO dto = new PersonDTO();
                        dto.setIdPerson(person.getIdPerson());
                        dto.setName(person.getName());
                        dto.setSurname(person.getSurname());
                        dto.setAge(person.getAge());
                        dto.setBirthdate(person.getBirthdate());
                        dto.setTypeDocument(person.getTypeDocument());
                        dto.setDocumentNumber(person.getDocumentNumber());
                        dto.setTypeKinship(person.getTypeKinship());
                        dto.setSponsored(person.getSponsored());
                        dto.setState(person.getState());
                        dto.setFamily(family);
                        return dto;
                    });
            });
    }

    public Flux<Person> findAllPersonsWithNullFamilyId() {
        return personRepository.findAll()
            .filter(person -> person.getFamilyIdFamily() == null);
    }       

    public Mono<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    public Flux<Person> saveAll(Flux<Person> persons) {
        return persons.map(person -> {
            person.setState("A");
            person.setFamilyIdFamily(null);
            return person;
        }).flatMap(personRepository::save);
    }

    public Flux<Person> findByFamilyId(Integer familyId) {
        return personRepository.findByFamilyId(familyId);
    }

    public Mono<Person> update(Integer id, Person person) {
        return personRepository.findById(id)
            .flatMap(existingPerson -> {
                existingPerson.setName(person.getName());
                existingPerson.setSurname(person.getSurname());
                existingPerson.setAge(person.getAge());
                existingPerson.setBirthdate(person.getBirthdate());
                existingPerson.setTypeDocument(person.getTypeDocument());
                existingPerson.setDocumentNumber(person.getDocumentNumber());
                existingPerson.setTypeKinship(person.getTypeKinship());
                existingPerson.setSponsored(person.getSponsored());
                existingPerson.setState("A");
                existingPerson.setFamilyIdFamily(person.getFamilyIdFamily());
                return personRepository.save(existingPerson);
            })
            .switchIfEmpty(Mono.empty());
    }

    public Mono<Void> updateFamilyIdForPersons(Integer familyId, List<Integer> personIds) {
        return personRepository.findAllById(personIds)
        .flatMap( person -> {
            person.setFamilyIdFamily(familyId);
            return personRepository.save(person);
        })
        .then();
    }
    
    public Mono<Person> logicalDelete(Integer id) {
        return personRepository.findById(id)
            .flatMap(existingPerson -> {
                existingPerson.setState("I");  // Set state as Inactive
                return personRepository.save(existingPerson);
            });
    }

    public Flux<PersonDTO> assignFamilyToNullPersons(Integer familyId) {
        return personRepository.findPersonsWithNullFamilyId()
                .map(person -> {
                    person.setFamilyIdFamily(familyId);  // Ensure it's Integer, not Long
                    return person;
                })
                .flatMap(personRepository::save)
                .map(this::convertToDTO);
    }


}
