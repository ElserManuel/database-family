package pe.edu.vallegrande.database.dto;

import lombok.Data;
import pe.edu.vallegrande.database.model.BasicService;
import pe.edu.vallegrande.database.model.CommunityEnvironment;
import pe.edu.vallegrande.database.model.FamilyComposition;
import pe.edu.vallegrande.database.model.FamilyFeeding;
import pe.edu.vallegrande.database.model.FamilyHealth;
import pe.edu.vallegrande.database.model.HousingDistribution;
import pe.edu.vallegrande.database.model.HousingEnvironment;
import pe.edu.vallegrande.database.model.LaborAutonomy;
import pe.edu.vallegrande.database.model.SocialLife;

@Data
public class FamilyDTO {
    private Integer id;
    private String direction;
    private String reasibAdmission;
    private String status;

    private BasicService basicService;
    private CommunityEnvironment communityEnvironment;
    private FamilyComposition familyComposition;
    private FamilyFeeding familyFeeding;
    private FamilyHealth familyHealth;
    private HousingDistribution housingDistribution;
    private HousingEnvironment housingEnvironment;
    private LaborAutonomy laborAutonomy;
    private SocialLife socialLife;
}
