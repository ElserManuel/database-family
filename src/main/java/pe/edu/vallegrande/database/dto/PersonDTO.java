package pe.edu.vallegrande.database.dto;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.Data;
import pe.edu.vallegrande.database.model.Family;

@Data
public class PersonDTO {
    @Id
    private Integer idPerson;
    private String name;
    private String surname;
    private Integer age;
    private LocalDate birthdate;
    private String typeDocument;
    private String documentNumber;
    private String typeKinship;
    private String sponsored;
    private String state;
    private Family family;
}
