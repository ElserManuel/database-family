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
@Table("housing_distribution")
public class HousingDistribution {
    @Id
    private Integer id;
    
    @Column("home_environment")
    private Integer homeEnvironment;
    
    @Column("bedroom_number")
    private Integer bedroomNumber;
    
    @Column("habitability")
    private String habitability;
}