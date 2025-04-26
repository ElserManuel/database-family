package pe.edu.vallegrande.database.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.vallegrande.database.model.Person;
import pe.edu.vallegrande.database.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PersonControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(personController).build();
    }

    @Test
    public void testGetActivePersons() {
        // Datos de prueba
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1));
        persons.add(new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1));

        // Configuración del mock
        when(personService.listActive()).thenReturn(Flux.fromIterable(persons));

        // Ejecución y verificación
        webTestClient.get()
                .uri("/api/v1/person/active")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(2)
                .contains(persons.toArray(new Person[0]));
    }

    @Test
    public void testGetInactivePersons() {
        // Datos de prueba
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1));
        persons.add(new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "I", 1));

        // Configuración del mock
        when(personService.listInactive()).thenReturn(Flux.fromIterable(persons));

        // Ejecución y verificación
        webTestClient.get()
                .uri("/api/v1/person/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(2)
                .contains(persons.toArray(new Person[0]));
    }

    @Test
    public void testCreatePersons() {
        // Datos de prueba
        List<Person> inputPersons = new ArrayList<>();
        inputPersons.add(new Person(null, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", null, 1));
        inputPersons.add(new Person(null, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", null, 1));

        List<Person> outputPersons = new ArrayList<>();
        outputPersons.add(new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1));
        outputPersons.add(new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1));

        // Configuración del mock
        when(personService.createPersons(any())).thenReturn(Flux.fromIterable(outputPersons));

        // Ejecución y verificación
        webTestClient.post()
                .uri("/api/v1/person")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inputPersons)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Person.class)
                .hasSize(2)
                .contains(outputPersons.toArray(new Person[0]));
    }

    @Test
    public void testUpdatePerson() {
        // Datos de prueba
        Person updateRequest = new Person(null, "Juan Carlos", "Pérez Gómez", 31,
                LocalDate.of(1993, 1, 1), "DNI", "12345678", "Padre", "No", "A", 2);

        Person updatedPerson = new Person(1, "Juan Carlos", "Pérez Gómez", 31,
                LocalDate.of(1993, 1, 1), "DNI", "12345678", "Padre", "No", "A", 2);

        // Configuración del mock
        when(personService.updatePerson(anyInt(), any(Person.class))).thenReturn(Mono.just(updatedPerson));

        // Ejecución y verificación
        webTestClient.put()
                .uri("/api/v1/person/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(updatedPerson);
    }

    @Test
    public void testDeletePerson() {
        // Datos de prueba
        Person deletedPerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "I", 1);

        // Configuración del mock
        when(personService.logicallyDelete(anyInt())).thenReturn(Mono.just(deletedPerson));

        // Ejecución y verificación
        webTestClient.delete()
                .uri("/api/v1/person/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(deletedPerson);
    }

    @Test
    public void testReactivatePerson() {
        // Datos de prueba
        Person reactivatedPerson = new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1);

        // Configuración del mock
        when(personService.reactivate(anyInt())).thenReturn(Mono.just(reactivatedPerson));

        // Ejecución y verificación
        webTestClient.put()
                .uri("/api/v1/person/{id}/reactivate", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(reactivatedPerson);
    }

    @Test
    public void testGetPersonsByFamilyId() {
        // Datos de prueba
        List<Person> familyMembers = new ArrayList<>();
        familyMembers.add(new Person(1, "Juan", "Pérez", 30, LocalDate.of(1993, 1, 1),
                "DNI", "12345678", "Padre", "No", "A", 1));
        familyMembers.add(new Person(2, "María", "López", 28, LocalDate.of(1995, 5, 15),
                "DNI", "87654321", "Madre", "No", "A", 1));

        // Configuración del mock
        when(personService.listByFamily(anyInt())).thenReturn(Flux.fromIterable(familyMembers));

        // Ejecución y verificación
        webTestClient.get()
                .uri("/api/v1/person/family/{familyId}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(2)
                .contains(familyMembers.toArray(new Person[0]));
    }
}