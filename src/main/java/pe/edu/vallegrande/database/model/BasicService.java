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
@Table("basic_service")
public class BasicService {
    @Id
    @Column("service_id")
    private Integer serviceId;
    
    @Column("water_service")
    private String waterService;
    
    @Column("serv_drain")
    private String servDrain;
    
    @Column("serv_light")
    private String servLight;
    
    @Column("serv_cable")
    private String servCable;
    
    @Column("serv_gas")
    private String servGas;
}