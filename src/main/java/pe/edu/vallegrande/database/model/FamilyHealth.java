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
@Table("family_health")
public class FamilyHealth {
    @Id
    private Integer id;
    
    @Column("safe_type")
    private String safeType;
    
    @Column("family_disease")
    private String familyDisease;
    
    @Column("treatment")
    private String treatment;
    
    @Column("antecedentes_enfermedad")
    private String antecedentesEnfermedad;
    
    @Column("examen_medico")
    private String examenMedico;
}