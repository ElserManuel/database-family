package pe.edu.vallegrande.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.database.dto.PersonDTO;
import pe.edu.vallegrande.database.model.Person;
import pe.edu.vallegrande.database.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/active")
    public Flux<PersonDTO> getAllActive() {
        return personService.findAllActivePersonsWithFamily();
    }

    @GetMapping("/inactive")
    public Flux<PersonDTO> getAllInactive() {
        return personService.findAllInactive();
    }

    @GetMapping("/family/{familyId}")
    public Flux<Person> getByFamilyId(@PathVariable Integer familyId) {
        return personService.findByFamilyId(familyId);
    }

    @GetMapping("/{id}")
    public Mono<Person> getById(@PathVariable Integer id) {
        return personService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Person> createPerson(@RequestBody Flux<Person> persons) {
        return personService.saveAll(persons);
    }

    @PutMapping("/{id}")
    public Mono<Person> update(@PathVariable Integer id, @RequestBody Person person) {
        return personService.update(id, person);
    }

    @GetMapping("/with-null-family-id")
    public Flux<Person> getPersonsWithNullFamilyId() {
        return personService.findAllPersonsWithNullFamilyId();
    }

    @DeleteMapping("/{id}")
    public Mono<Person> delete(@PathVariable Integer id) {
        return personService.logicalDelete(id);
    }

    // Actualizar FamilyIdFamily de varias personas
    @PutMapping("/update-family/{familyId}")
    public Mono<ResponseEntity<Void>> updateFamilyIdForPersons(@PathVariable Integer familyId, @RequestBody List<Integer> personIds) {
        return personService.updateFamilyIdForPersons(familyId, personIds)
        .thenReturn(ResponseEntity.ok().build());
    }

    @PutMapping("/assign-family/{familyId}")
    public Flux<PersonDTO> assignFamilyToNullPersons(@PathVariable Integer familyId) {
        return personService.assignFamilyToNullPersons(familyId);
    }


}
