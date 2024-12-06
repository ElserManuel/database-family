package pe.edu.vallegrande.database.service;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.database.dto.FamilyDTO;
import pe.edu.vallegrande.database.model.*;
import pe.edu.vallegrande.database.repository.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FamilyService {

    private final BasicServiceRepository basicServiceRepository;
    private final CommunityEnvironmentRepository communityEnvironmentRepository;
    private final FamilyCompositionRepository familyCompositionRepository;
    private final FamilyFeedingRepository familyFeedingRepository;
    private final FamilyHealthRepository familyHealthRepository;
    private final HousingDistributionRepository housingDistributionRepository;
    private final HousingEnvironmentRepository housingEnvironmentRepository;
    private final LaborAutonomyRepository laborAutonomyRepository;
    private final SocialLifeRepository socialLifeRepository;
    private final FamilyRepository familyRepository; // Añade el repositorio de Family

    public FamilyService(BasicServiceRepository basicServiceRepository,
                         CommunityEnvironmentRepository communityEnvironmentRepository,
                         FamilyCompositionRepository familyCompositionRepository,
                         FamilyFeedingRepository familyFeedingRepository,
                         FamilyHealthRepository familyHealthRepository,
                         HousingDistributionRepository housingDistributionRepository,
                         HousingEnvironmentRepository housingEnvironmentRepository,
                         LaborAutonomyRepository laborAutonomyRepository,
                         SocialLifeRepository socialLifeRepository,
                         FamilyRepository familyRepository) { // Añade el repositorio al constructor
        this.basicServiceRepository = basicServiceRepository;
        this.communityEnvironmentRepository = communityEnvironmentRepository;
        this.familyCompositionRepository = familyCompositionRepository;
        this.familyFeedingRepository = familyFeedingRepository;
        this.familyHealthRepository = familyHealthRepository;
        this.housingDistributionRepository = housingDistributionRepository;
        this.housingEnvironmentRepository = housingEnvironmentRepository;
        this.laborAutonomyRepository = laborAutonomyRepository;
        this.socialLifeRepository = socialLifeRepository;
        this.familyRepository = familyRepository; // Inicializa el repositorio
    }

    public Mono<FamilyDTO> populateRelatedEntities(Family family) {
        FamilyDTO dto = new FamilyDTO();
        dto.setId(family.getId());
        dto.setDirection(family.getDirection());
        dto.setReasibAdmission(family.getReasibAdmission());
        dto.setStatus(family.getStatus());
    
        return basicServiceRepository.findById(family.getBasicServiceServiceId())
            .flatMap(basicService -> {
                dto.setBasicService(basicService); // Asigna la entidad completa
                return communityEnvironmentRepository.findById(family.getCommunityEnvironmentId());
            })
            .flatMap(communityEnvironment -> {
                dto.setCommunityEnvironment(communityEnvironment); // Asigna la entidad completa
                return familyCompositionRepository.findById(family.getFamilyCompositionId());
            })
            .flatMap(familyComposition -> {
                dto.setFamilyComposition(familyComposition); // Asigna la entidad completa
                return familyFeedingRepository.findById(family.getFamilyFeedingId());
            })
            .flatMap(familyFeeding -> {
                dto.setFamilyFeeding(familyFeeding); // Asigna la entidad completa
                return familyHealthRepository.findById(family.getFamilyHealthId());
            })
            .flatMap(familyHealth -> {
                dto.setFamilyHealth(familyHealth); // Asigna la entidad completa
                return housingDistributionRepository.findById(family.getHousingDistributionId());
            })
            .flatMap(housingDistribution -> {
                dto.setHousingDistribution(housingDistribution); // Asigna la entidad completa
                return housingEnvironmentRepository.findById(family.getHousingEnvironmentId());
            })
            .flatMap(housingEnvironment -> {
                dto.setHousingEnvironment(housingEnvironment); // Asigna la entidad completa
                return laborAutonomyRepository.findById(family.getLaborAutonomyId());
            })
            .flatMap(laborAutonomy -> {
                dto.setLaborAutonomy(laborAutonomy); // Asigna la entidad completa
                return socialLifeRepository.findById(family.getSocialLifeId());
            })
            .map(socialLife -> {
                dto.setSocialLife(socialLife); // Asigna la entidad completa
                return dto; // Retorna el DTO completo
            });
    }
    
    // Método para obtener un listado general de FamilyDTO
    public Flux<FamilyDTO> findAllActive() {
        return familyRepository.findAllByStatus("A")
            .flatMap(this::populateRelatedEntities);
    }

    public Flux<FamilyDTO> findAllInactive() {
        return familyRepository.findAllByStatus("I")
            .flatMap(this::populateRelatedEntities);
    }

    public Mono<FamilyDTO> findById(Integer id) {
        return familyRepository.findById(id)
            .flatMap(this::populateRelatedEntities);
    }

    public Mono<FamilyDTO> createFamily(FamilyDTO familyDTO) {
        // Save related entities sequentially
        return basicServiceRepository.save(familyDTO.getBasicService())
            .flatMap(savedBasicService -> {
                familyDTO.getBasicService().setServiceId(savedBasicService.getServiceId());
                return communityEnvironmentRepository.save(familyDTO.getCommunityEnvironment());
            })
            .flatMap(savedCommunityEnvironment -> {
                familyDTO.getCommunityEnvironment().setId(savedCommunityEnvironment.getId());
                return familyCompositionRepository.save(familyDTO.getFamilyComposition());
            })
            .flatMap(savedFamilyComposition -> {
                familyDTO.getFamilyComposition().setId(savedFamilyComposition.getId());
                return familyFeedingRepository.save(familyDTO.getFamilyFeeding());
            })
            .flatMap(savedFamilyFeeding -> {
                familyDTO.getFamilyFeeding().setId(savedFamilyFeeding.getId());
                return familyHealthRepository.save(familyDTO.getFamilyHealth());
            })
            .flatMap(savedFamilyHealth -> {
                familyDTO.getFamilyHealth().setId(savedFamilyHealth.getId());
                return housingDistributionRepository.save(familyDTO.getHousingDistribution());
            })
            .flatMap(savedHousingDistribution -> {
                familyDTO.getHousingDistribution().setId(savedHousingDistribution.getId());
                return housingEnvironmentRepository.save(familyDTO.getHousingEnvironment());
            })
            .flatMap(savedHousingEnvironment -> {
                familyDTO.getHousingEnvironment().setId(savedHousingEnvironment.getId());
                return laborAutonomyRepository.save(familyDTO.getLaborAutonomy());
            })
            .flatMap(savedLaborAutonomy -> {
                familyDTO.getLaborAutonomy().setId(savedLaborAutonomy.getId());
                return socialLifeRepository.save(familyDTO.getSocialLife());
            })
            .flatMap(savedSocialLife -> {
                familyDTO.getSocialLife().setId(savedSocialLife.getId());
                
                // Create Family entity with saved IDs
                Family family = new Family();
                family.setDirection(familyDTO.getDirection());
                family.setReasibAdmission(familyDTO.getReasibAdmission());
                family.setStatus("A");
                family.setBasicServiceServiceId(familyDTO.getBasicService().getServiceId());
                family.setCommunityEnvironmentId(familyDTO.getCommunityEnvironment().getId());
                family.setFamilyCompositionId(familyDTO.getFamilyComposition().getId());
                family.setFamilyFeedingId(familyDTO.getFamilyFeeding().getId());
                family.setFamilyHealthId(familyDTO.getFamilyHealth().getId());
                family.setHousingDistributionId(familyDTO.getHousingDistribution().getId());
                family.setHousingEnvironmentId(familyDTO.getHousingEnvironment().getId());
                family.setLaborAutonomyId(familyDTO.getLaborAutonomy().getId());
                family.setSocialLifeId(familyDTO.getSocialLife().getId());
    
                return familyRepository.save(family);
            })
            .flatMap(this::populateRelatedEntities)
            .onErrorResume(e -> {
                e.printStackTrace(); // Log full stack trace
                return Mono.error(new RuntimeException("Error during family creation: " + e.getMessage()));
            });
    }

    
    public Mono<FamilyDTO> updateFamily(Integer id, FamilyDTO familyDTO) {
        return familyRepository.findById(id)
            .flatMap(existingFamily -> {
                // Actualiza los campos de la familia
                existingFamily.setDirection(familyDTO.getDirection());
                existingFamily.setReasibAdmission(familyDTO.getReasibAdmission());
                existingFamily.setStatus("A");

                // Guarda la familia actualizada
                return familyRepository.save(existingFamily);
            })
            .flatMap(updatedFamily -> {
                // Actualiza las entidades relacionadas sin necesidad de IDs
                return basicServiceRepository.findById(updatedFamily.getBasicServiceServiceId())
                    .flatMap(existingBasicService -> {
                        existingBasicService.setWaterService(familyDTO.getBasicService().getWaterService());
                        existingBasicService.setServDrain(familyDTO.getBasicService().getServDrain());
                        existingBasicService.setServLight(familyDTO.getBasicService().getServLight());
                        existingBasicService.setServCable(familyDTO.getBasicService().getServCable());
                        existingBasicService.setServGas(familyDTO.getBasicService().getServGas());
                        return basicServiceRepository.save(existingBasicService);
                    })
                    .then(communityEnvironmentRepository.findById(updatedFamily.getCommunityEnvironmentId())
                        .flatMap(existingCommunityEnvironment -> {
                            existingCommunityEnvironment.setArea(familyDTO.getCommunityEnvironment().getArea());
                            existingCommunityEnvironment.setReferenceLocation(familyDTO.getCommunityEnvironment().getReferenceLocation());
                            existingCommunityEnvironment.setResidue(familyDTO.getCommunityEnvironment().getResidue());
                            existingCommunityEnvironment.setPublicLighting(familyDTO.getCommunityEnvironment().getPublicLighting());
                            existingCommunityEnvironment.setSecurity(familyDTO.getCommunityEnvironment().getSecurity());
                            return communityEnvironmentRepository.save(existingCommunityEnvironment);
                        })
                    )
                    .then(familyCompositionRepository.findById(updatedFamily.getFamilyCompositionId())
                        .flatMap(existingFamilyComposition -> {
                            existingFamilyComposition.setNumberMembers(familyDTO.getFamilyComposition().getNumberMembers());
                            existingFamilyComposition.setNumberChildren(familyDTO.getFamilyComposition().getNumberChildren());
                            existingFamilyComposition.setFamilyType(familyDTO.getFamilyComposition().getFamilyType());
                            existingFamilyComposition.setSocialProblems(familyDTO.getFamilyComposition().getSocialProblems());
                            return familyCompositionRepository.save(existingFamilyComposition);
                        })
                    )
                    .then(familyFeedingRepository.findById(updatedFamily.getFamilyFeedingId())
                        .flatMap(existingFamilyFeeding -> {
                            existingFamilyFeeding.setFrecuenciaSemanal(familyDTO.getFamilyFeeding().getFrecuenciaSemanal());
                            existingFamilyFeeding.setTipoAlimentacion(familyDTO.getFamilyFeeding().getTipoAlimentacion());
                            return familyFeedingRepository.save(existingFamilyFeeding);
                        })
                    )
                    .then(familyHealthRepository.findById(updatedFamily.getFamilyHealthId())
                        .flatMap(existingFamilyHealth -> {
                            existingFamilyHealth.setSafeType(familyDTO.getFamilyHealth().getSafeType());
                            existingFamilyHealth.setFamilyDisease(familyDTO.getFamilyHealth().getFamilyDisease());
                            existingFamilyHealth.setTreatment(familyDTO.getFamilyHealth().getTreatment());
                            existingFamilyHealth.setAntecedentesEnfermedad(familyDTO.getFamilyHealth().getAntecedentesEnfermedad());
                            existingFamilyHealth.setExamenMedico(familyDTO.getFamilyHealth().getExamenMedico());
                            return familyHealthRepository.save(existingFamilyHealth);
                        })
                    )
                    .then(housingDistributionRepository.findById(updatedFamily.getHousingDistributionId())
                        .flatMap(existingHousingDistribution -> {
                            existingHousingDistribution.setAmbienteHogar(familyDTO.getHousingDistribution().getAmbienteHogar());
                            existingHousingDistribution.setNumeroDormitorio(familyDTO.getHousingDistribution().getNumeroDormitorio());
                            existingHousingDistribution.setHabitabilidad(familyDTO.getHousingDistribution().getHabitabilidad());
                            return housingDistributionRepository.save(existingHousingDistribution);
                        })
                    )
                    .then(housingEnvironmentRepository.findById(updatedFamily.getHousingEnvironmentId())
                        .flatMap(existingHousingEnvironment -> {
                            existingHousingEnvironment.setTenure(familyDTO.getHousingEnvironment().getTenure());
                            existingHousingEnvironment.setTypeOfHousing(familyDTO.getHousingEnvironment().getTypeOfHousing());
                            existingHousingEnvironment.setHousingMaterial(familyDTO.getHousingEnvironment().getHousingMaterial());
                            existingHousingEnvironment.setHousingSecurity(familyDTO.getHousingEnvironment().getHousingSecurity());
                            return housingEnvironmentRepository.save(existingHousingEnvironment);
                        })
                    )
                    .then(laborAutonomyRepository.findById(updatedFamily.getLaborAutonomyId())
                        .flatMap(existingLaborAutonomy -> {
                            existingLaborAutonomy.setNumberRooms(familyDTO.getLaborAutonomy().getNumberRooms());
                            existingLaborAutonomy.setNumberOfBedrooms(familyDTO.getLaborAutonomy().getNumberOfBedrooms());
                            existingLaborAutonomy.setHabitabilityBuilding(familyDTO.getLaborAutonomy().getHabitabilityBuilding());
                            return laborAutonomyRepository.save(existingLaborAutonomy);
                        })
                    )
                    .then(socialLifeRepository.findById(updatedFamily.getSocialLifeId())
                        .flatMap(existingSocialLife -> {
                            existingSocialLife.setMaterial(familyDTO.getSocialLife().getMaterial());
                            existingSocialLife.setFeeding(familyDTO.getSocialLife().getFeeding());
                            existingSocialLife.setEconomic(familyDTO.getSocialLife().getEconomic());
                            existingSocialLife.setSpiritual(familyDTO.getSocialLife().getSpiritual());
                            existingSocialLife.setSocialCompany(familyDTO.getSocialLife().getSocialCompany());
                            existingSocialLife.setGuideTip(familyDTO.getSocialLife().getGuideTip());
                            return socialLifeRepository.save(existingSocialLife);
                        })
                    )
                    .then(populateRelatedEntities(updatedFamily)); // Devuelve el DTO completo
            })
            .onErrorResume(e -> {
                e.printStackTrace(); // Log full stack trace
                return Mono.error(new RuntimeException("Error during family update: " + e.getMessage()));
            });
    }    

    public Mono<Void> deleteFamily(Integer id) {
        return familyRepository.findById(id)
            .flatMap(family -> {
                family.setStatus("I");
                return familyRepository.save(family).then();
            });
    }

    public Mono<Void> activeFamily(Integer id) {
        return familyRepository.findById(id)
            .flatMap(family -> {
                family.setStatus("A");
                return familyRepository.save(family).then();
            });
    }

    public Mono<FamilyDTO> findDetailById(Integer id) {
        return familyRepository.findById(id)
                .flatMap(this::populateRelatedEntities);
    }

}
