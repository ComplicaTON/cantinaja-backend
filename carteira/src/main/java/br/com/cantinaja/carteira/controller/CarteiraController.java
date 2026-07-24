package br.com.cantinaja.carteira.controller;

import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.service.CarteiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @PostMapping(value="/{alunoId}/recargas", version = "1.0.0")
    public ResponseEntity<CarteiraResponseDTO> recarregar(
            @PathVariable Long alunoId,
            @Valid @RequestBody RecargaRequestDTO request
    ) {
        CarteiraResponseDTO response = carteiraService.recarregar(alunoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}