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
@Table("family_feeding")
public class FamilyFeeding {
    @Id
    private Integer id;
    
    @Column("frecuencia_semanal")
    private String frecuenciaSemanal;
    
    @Column("tipo_alimentacion")
    private String tipoAlimentacion;
}