package br.com.cantinaja.cardapio.service;

import br.com.cantinaja.cardapio.dto.SugestaoRequestDTO;
import br.com.cantinaja.cardapio.model.Sugestao;
import br.com.cantinaja.cardapio.repository.SugestaoRepository;
import br.com.cantinaja.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SugestaoService {
    private final SugestaoRepository repository;

    public SugestaoService(SugestaoRepository repository) {
        this.repository = repository;
    }

    public Sugestao criar(SugestaoRequestDTO dto){
        String descricaoSanitizada = dto.descricao().strip();

        if (repository.existsByDescricaoIgnoreCase(descricaoSanitizada)) {
            throw new BusinessException(HttpStatus.CONFLICT,
                    "SUGESTÃO_DUPLICADA",
                    "Essa sugestão ja existe.");
        }

        Sugestao sugestao = new Sugestao(descricaoSanitizada, dto.alunoId(), LocalDateTime.now());

        Sugestao sugestaoSalva = repository.save(sugestao);
        return sugestaoSalva;
    }

    public List<Sugestao> listar(){
        return repository.findAllByOrderByDataHoraDesc();
    }

}
