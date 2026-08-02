
package br.com.cantinaja.carteira.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.cantinaja.carteira.dto.DebitoRequest;
import br.com.cantinaja.carteira.service.CarteiraService;

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @PostMapping("/{alunoId}/debitos")
    public ResponseEntity<DebitoRequest> debitar(
            @PathVariable Long alunoId,
            @Valid @RequestBody DebitoRequest request) {
        carteiraService.debitar(alunoId, request.valor());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

