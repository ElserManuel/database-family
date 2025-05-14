package pe.edu.vallegrande.database.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.database.client.FamilyServiceClient;
import pe.edu.vallegrande.database.model.Person;
import pe.edu.vallegrande.database.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

 class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FamilyServiceClient familyServiceClient;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testListActive() {
        // Datos de prueba
        Person person1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person person2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1);

        // Configuración del mock
        when(personRepository.findByState("A")).thenReturn(Flux.just(person2, person1));

        // Ejecución de la prueba
        Flux<Person> result = personService.listActive();

        // Verificación
        StepVerifier.create(result)
                .expectNext(person1) // Ordenado por ID, primero person1
                .expectNext(person2)
                .verifyComplete();
    }

    @Test
     void testListInactive() {
        // Datos de prueba
        Person person1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);
        Person person2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "I", 1);

        // Configuración del mock
        when(personRepository.findByState("I")).thenReturn(Flux.just(person2, person1));

        // Ejecución de la prueba
        Flux<Person> result = personService.listInactive();

        // Verificación
        StepVerifier.create(result)
                .expectNext(person1) // Ordenado por ID, primero person1
                .expectNext(person2)
                .verifyComplete();
    }

    @Test
     void testCreatePersons_WithValidFamily() {
        // Datos de prueba
        Person person = new Person(null, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", null, 1);
        Person savedPerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);

        // Configuración de los mocks
        when(familyServiceClient.familyExists(1)).thenReturn(Mono.just(true));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(savedPerson));

        // Ejecución de la prueba
        Flux<Person> result = personService.createPersons(Flux.just(person));

        // Verificación
        StepVerifier.create(result)
                .expectNext(savedPerson)
                .verifyComplete();
    }

    @Test
    void testCreatePersons_WithInvalidFamily() {
        // Datos de prueba
        Person person = new Person(null, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", null, 999);

        // Configuración de los mocks
        when(familyServiceClient.familyExists(999)).thenReturn(Mono.just(false));

        // Ejecución de la prueba
        Flux<Person> result = personService.createPersons(Flux.just(person));

        // Verificación
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("familia con ID 999 no existe"))
                .verify();
    }

    @Test
     void testCreatePersons_WithoutFamily() {
        // Datos de prueba
        Person person = new Person(null, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", null, null);
        Person savedPerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", null);

        // Configuración del mock
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(savedPerson));

        // Ejecución de la prueba
        Flux<Person> result = personService.createPersons(Flux.just(person));

        // Verificación
        StepVerifier.create(result)
                .expectNext(savedPerson)
                .verifyComplete();
    }

    @Test
     void testLogicallyDelete() {
        // Datos de prueba
        Person person = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person inactivePerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);

        // Configuración de los mocks
        when(personRepository.findById(1)).thenReturn(Mono.just(person));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(inactivePerson));

        // Ejecución de la prueba
        Mono<Person> result = personService.logicallyDelete(1);

        // Verificación
        StepVerifier.create(result)
                .expectNext(inactivePerson)
                .verifyComplete();
    }

    @Test
     void testReactivate() {
        // Datos de prueba
        Person person = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);
        Person activePerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);

        // Configuración de los mocks
        when(personRepository.findById(1)).thenReturn(Mono.just(person));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(activePerson));

        // Ejecución de la prueba
        Mono<Person> result = personService.reactivate(1);

        // Verificación
        StepVerifier.create(result)
                .expectNext(activePerson)
                .verifyComplete();
    }

    @Test
     void testListByFamily() {
        // Datos de prueba
        Person person1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person person2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1);

        // Configuración del mock
        when(personRepository.findByFamilyIdFamily(1)).thenReturn(Flux.just(person1, person2));

        // Ejecución de la prueba
        Flux<Person> result = personService.listByFamily(1);

        // Verificación
        StepVerifier.create(result)
                .expectNext(person1)
                .expectNext(person2)
                .verifyComplete();
    }

    @Test
     void testUpdatePerson() {
        // Datos de prueba
        Person originalPerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person updatedPersonData = new Person(null, "Juan Carlos", "Pérez Gómez", 31, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 2);
        Person resultPerson = new Person(1, "Juan Carlos", "Pérez Gómez", 31, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 2);

        // Configuración de los mocks
        when(personRepository.findById(1)).thenReturn(Mono.just(originalPerson));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(resultPerson));

        // Ejecución de la prueba
        Mono<Person> result = personService.updatePerson(1, updatedPersonData);

        // Verificación
        StepVerifier.create(result)
                .expectNext(resultPerson)
                .verifyComplete();
    }

    @Test
     void testDeletePersonsByFamilyId() {
        // Datos de prueba
        Person person1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person person2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1);

        Person inactivePerson1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);
        Person inactivePerson2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "I", 1);

        // Configuración de los mocks
        when(personRepository.findByFamilyIdFamily(1)).thenReturn(Flux.just(person1, person2));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(inactivePerson1), Mono.just(inactivePerson2));

        // Ejecución de la prueba
        Flux<Person> result = personService.deletePersonsByFamilyId(1);

        // Verificación
        StepVerifier.create(result)
                .expectNext(inactivePerson1)
                .expectNext(inactivePerson2)
                .verifyComplete();
    }

    @Test
     void testReactivatePersonsByFamilyId() {
        // Datos de prueba
        Person person1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);
        Person person2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "I", 1);

        Person activePerson1 = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);
        Person activePerson2 = new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1);

        // Configuración de los mocks
        when(personRepository.findByFamilyIdFamily(1)).thenReturn(Flux.just(person1, person2));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(activePerson1), Mono.just(activePerson2));

        // Ejecución de la prueba
        Flux<Person> result = personService.reactivatePersonsByFamilyId(1);

        // Verificación
        StepVerifier.create(result)
                .expectNext(activePerson1)
                .expectNext(activePerson2)
                .verifyComplete();
    }
}