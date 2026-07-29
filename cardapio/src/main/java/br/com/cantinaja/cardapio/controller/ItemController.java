package br.com.cantinaja.cardapio.controller;

import br.com.cantinaja.cardapio.controller.swagger.ItemControllerSwagger;
import br.com.cantinaja.cardapio.dto.ItemRequestDTO;
import br.com.cantinaja.cardapio.dto.ItemResponseDTO;
import br.com.cantinaja.cardapio.dto.ItemUpdateRequestDTO;
import br.com.cantinaja.cardapio.model.Item;
import br.com.cantinaja.cardapio.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/{version}/itens")
public class ItemController implements ItemControllerSwagger {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ItemResponseDTO>> listarTodos(@RequestParam(required = false) Boolean disponivel, @PageableDefault(size = 10) Pageable pageable) {
        Page<ItemResponseDTO> resposta = service
                .listar(disponivel, pageable)
                .map(ItemResponseDTO::fromEntity);
        return ResponseEntity.ok(resposta);
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid ItemRequestDTO dto, HttpServletRequest request) {
        Item item = service.cadastrar(dto);
        URI location = URI.create(request.getRequestURI() + "/" + item.getId());
        return ResponseEntity.created(location).build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Item> atualizar(@PathVariable Long id, @Valid @RequestBody ItemUpdateRequestDTO dto) {
        Item itemAtualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(itemAtualizado);
    }
}
