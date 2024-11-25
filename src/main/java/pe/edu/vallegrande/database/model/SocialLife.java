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
@Table("social_life")
public class SocialLife {
    @Id
    private Integer id;

    @Column("material")
    private String material;

    @Column("feeding")
    private String feeding;

    @Column("economic")
    private String economic;

    @Column("spiritual")
    private String spiritual;
    
    @Column("social_company")
    private String socialCompany;
    
    @Column("guide_tip")
    private String guideTip;
}