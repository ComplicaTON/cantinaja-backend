package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.controller.swagger.ItemControllerSwagger;
import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/{version}/itens")
public class ItemController implements ItemControllerSwagger {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> cadastrarItem(@RequestBody @Valid ItemRequestDTO dto, HttpServletRequest request) {
        Item item = service.criar(dto);
        URI location = URI.create(request.getRequestURI() + "/" + item.getId());
        return ResponseEntity.created(location).build();
    }
}
