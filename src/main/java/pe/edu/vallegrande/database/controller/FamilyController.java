package pe.edu.vallegrande.database.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.database.dto.FamilyDTO;
import pe.edu.vallegrande.database.service.FamilyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/families")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @GetMapping("/active")
    public ResponseEntity<Flux<FamilyDTO>> getAllActiveFamilies() {
        Flux<FamilyDTO> families = familyService.findAllActive();
        return ResponseEntity.status(HttpStatus.OK).body(families);
    }

    @GetMapping("/inactive")
    public ResponseEntity<Flux<FamilyDTO>> getAllInactiveFamilies() {
        Flux<FamilyDTO> families = familyService.findAllInactive();
        return ResponseEntity.status(HttpStatus.OK).body(families);
    }

    @GetMapping("/detail/{id}")
    public Mono<ResponseEntity<FamilyDTO>> getFamilyDetailById(@PathVariable Integer id) {
        return familyService.findDetailById(id)
                .map(familyDTO -> ResponseEntity.ok(familyDTO))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FamilyDTO>> getFamilyById(@PathVariable Integer id) {
        return familyService.findById(id)
            .map(familyDTO -> ResponseEntity.ok(familyDTO))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<FamilyDTO>> createFamily(@RequestBody FamilyDTO familyDTO) {
        return familyService.createFamily(familyDTO)
            .map(savedFamilyDTO -> ResponseEntity.status(HttpStatus.CREATED).body(savedFamilyDTO))
            .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<FamilyDTO>> updateFamily(
            @PathVariable Integer id, 
            @RequestBody FamilyDTO familyDTO) {
        return familyService.updateFamily(id, familyDTO)
            .map(updatedFamilyDTO -> ResponseEntity.ok(updatedFamilyDTO))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteFamily(@PathVariable Integer id) {
        return familyService.deleteFamily(id) // Cambia este método en tu servicio para manejar el eliminado lógico
            .then(Mono.just(ResponseEntity.noContent().build()))
            .onErrorResume(e -> {
                if (e instanceof IllegalArgumentException) {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                }
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));
            });
    }

    @PutMapping("/active/{id}")
    public Mono<ResponseEntity<Object>> activeFamily(@PathVariable Integer id) {
        return familyService.activeFamily(id) // Cambia este método en tu servicio para manejar el eliminado lógico
            .then(Mono.just(ResponseEntity.noContent().build()))
            .onErrorResume(e -> {
                if (e instanceof IllegalArgumentException) {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                }
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));
            });
    }
}
