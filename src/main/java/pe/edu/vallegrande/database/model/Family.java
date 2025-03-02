package pe.edu.vallegrande.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("family")
public class Family {
    @Id
    private Integer id;
    private String direction;
    private String reasibAdmission;
    private String status;

    // IDs de las entidades relacionadas
    private Integer basicServiceServiceId;
    private Integer communityEnvironmentId;
    private Integer familyCompositionId;
    private Integer familyFeedingId;
    private Integer familyHealthId;
    private Integer housingDistributionId;
    private Integer housingEnvironmentId;
    private Integer laborAutonomyId;
    private Integer socialLifeId;
}