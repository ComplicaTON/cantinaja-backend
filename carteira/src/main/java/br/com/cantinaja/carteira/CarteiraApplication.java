package br.com.cantinaja.carteira;

import br.com.cantinaja.carteira.model.Carteira;
import br.com.cantinaja.carteira.repository.CarteiraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class CarteiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarteiraApplication.class, args);
	}

	@Bean
	public CommandLineRunner carregarDadosIniciais(CarteiraRepository carteiraRepository) {
		return args -> {
			// Cria a carteira do aluno 1 com saldo de R$ 100,00 se ela ainda não existir
			if (carteiraRepository.findByAlunoId(1L).isEmpty()) {
				carteiraRepository.save(new Carteira(1L, new BigDecimal("100.00")));
			}
		};
	}
}