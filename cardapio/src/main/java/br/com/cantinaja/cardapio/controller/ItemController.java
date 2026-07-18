package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.dto.ItemResponseDTO;
import br.com.cantinaja.cardapio.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<Void> cadastrarItem(@RequestBody @Valid ItemRequestDTO dto, HttpServletRequest request) {
        service.criar(dto);
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }
}
