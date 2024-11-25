package pe.edu.vallegrande.database.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.database.dto.FamilyDTO;
import pe.edu.vallegrande.database.service.FamilyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FamilyDTO>> getFamilyById(@PathVariable Integer id) {
        return familyService.findById(id) // Implementa este método en FamilyService
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
}
