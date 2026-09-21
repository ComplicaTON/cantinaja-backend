package br.com.cantinaja.carteira.controller;

import br.com.cantinaja.carteira.controller.swagger.CarteiraControllerSwagger;
import br.com.cantinaja.carteira.dto.CarteiraResponseDTO;
import br.com.cantinaja.carteira.dto.RecargaRequestDTO;
import br.com.cantinaja.carteira.dto.TransacaoResponseDTO;
import br.com.cantinaja.carteira.model.TipoTransacao;
import br.com.cantinaja.carteira.service.CarteiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.cantinaja.carteira.dto.DebitoRequest;
import br.com.cantinaja.carteira.service.CarteiraService;
import java.util.List;

@RestController
@RequestMapping("/api/{version}/carteiras")
public class CarteiraController implements CarteiraControllerSwagger {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @Override
    @PostMapping(value="/{alunoId}/recargas")
    public ResponseEntity<CarteiraResponseDTO> recarregar(
            @PathVariable Long alunoId,
            @Valid @RequestBody RecargaRequestDTO request
    ) {
        CarteiraResponseDTO response = carteiraService.recarregar(alunoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{alunoId}/debitos")
    public ResponseEntity<DebitoRequest> debitar(
            @PathVariable Long alunoId,
            @Valid @RequestBody DebitoRequest request
    ) {
        carteiraService.debitar(alunoId, request.valor());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

        List <TransacaoResponseDTO> transacoes = carteiraService.consultarTransacoes(alunoId, tipo);
        return ResponseEntity.ok(transacoes);
    }
}

    //@Override
    @GetMapping(value = "/{alunoId}")
    public ResponseEntity<CarteiraResponseDTO> consultar(@PathVariable Long alunoId) {
        CarteiraResponseDTO response = carteiraService.consultar(alunoId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}