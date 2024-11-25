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
@Table("community_environment")
public class CommunityEnvironment {
    @Id
    private Integer id;
    
    @Column("area")
    private String area;
    
    @Column("reference_location")
    private String referenceLocation;
    
    @Column("residue")
    private String residue;
    
    @Column("public_lighting")
    private String publicLighting;
    
    private String security;
}
