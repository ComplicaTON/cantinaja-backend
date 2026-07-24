package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.dto.SugestaoRequestDTO;
import br.com.cantinaja.cardapio.model.Sugestao;
import br.com.cantinaja.cardapio.service.SugestaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
