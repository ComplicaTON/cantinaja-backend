package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.dto.ItemRequest;
import br.com.cantinaja.cardapio.dto.ItemResponse;
import br.com.cantinaja.cardapio.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<ItemResponse> cadastrarItem(ItemRequest dto, HttpServletRequest request) throws URISyntaxException {
        ItemResponse response = service.criar(dto);
        return ResponseEntity.created(new URI(request.getRequestURI())).body(response);
    }

    // Continue...
}
