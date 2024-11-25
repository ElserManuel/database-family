package pe.edu.vallegrande.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
@Table("housing_environment")
public class HousingEnvironment {
    @Id
    private Integer id;
    
    @Column("tenure")
    private String tenure;
    
    @Column("type_of_housing")
    private String typeOfHousing;
    
    @Column("housing_material")
    private String housingMaterial;
    
    @Column("housing_security")
    private String housingSecurity;
}