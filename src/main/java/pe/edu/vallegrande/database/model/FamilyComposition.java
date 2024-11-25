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
@Table("family_composition")
public class FamilyComposition {
    @Id
    private Integer id;
    
    @Column("number_members")
    private Integer numberMembers;
    
    @Column("number_children")
    private Integer numberChildren;
    
    @Column("family_type")
    private String familyType;
    
    @Column("social_problems")
    private String socialProblems;
}