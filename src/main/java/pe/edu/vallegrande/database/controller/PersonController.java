package pe.edu.vallegrande.database.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.database.model.Person;
import pe.edu.vallegrande.database.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Person> listActive() {
        return personService.listActive();
    }

    @GetMapping(value = "/inactive", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Person> listInactive() {
        return personService.listInactive();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<Person>>> createPersons(@RequestBody Flux<Person> persons) {
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(personService.createPersons(persons)
                        .onErrorResume(error -> {
                            logError("Error al crear personas", error);
                            return Flux.empty();
                        })));
    }

    // Cambiado a DELETE
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Person>> logicallyDelete(@PathVariable Integer id) {
        return personService.logicallyDelete(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Cambiado a PUT y ajustada la ruta
    @PutMapping(value = "/{id}/reactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Person>> reactivate(@PathVariable Integer id) {
        return personService.reactivate(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/family/{familyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Person> listByFamily(@PathVariable Integer familyId) {
        return personService.listByFamily(familyId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Person>> updatePerson(@PathVariable Integer id, @RequestBody Person updatedPerson) {
        return personService.updatePerson(id, updatedPerson)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private void logError(String message, Throwable error) {
        System.err.println(message + ": " + error.getMessage());
    }
}
