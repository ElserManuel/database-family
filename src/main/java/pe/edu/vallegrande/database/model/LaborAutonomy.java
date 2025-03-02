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
@Table("labor_autonomy")
public class LaborAutonomy {
    @Id
    @Column("id")
    private Integer id;
    
    @Column("number_rooms")
    private Integer numberRooms;
    
    @Column("number_of_bedrooms")
    private Integer numberOfBedrooms;
    
    @Column("habitability_building")
    private String habitabilityBuilding;
}