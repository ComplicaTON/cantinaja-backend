package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.dto.SugestaoRequestDTO;
import br.com.cantinaja.cardapio.dto.SugestaoResponseDTO;
import br.com.cantinaja.cardapio.model.Sugestao;
import br.com.cantinaja.cardapio.service.SugestaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/{version}/sugestoes", version = "v1")
public class SugestaoController {
    private final SugestaoService service;

    public SugestaoController(SugestaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> criarSugestao(@RequestBody @Valid SugestaoRequestDTO dto,
                                              HttpServletRequest request) {
        Sugestao sugestao = service.criar(dto);
        URI location = URI.create(request.getRequestURI() + "/" + sugestao.getId());
        return ResponseEntity.created(location).build(); //201
    }

    @GetMapping
    public ResponseEntity<List<SugestaoResponseDTO>> listarSugestoes() {
        List<SugestaoResponseDTO> corpo = service.listar().stream()
                .map(SugestaoResponseDTO::dados)
                .toList();
        return ResponseEntity.ok(corpo);
    }
}
