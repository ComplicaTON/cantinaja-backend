package br.com.cantinaja.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção de regra de negócio. Carrega o status HTTP, um código estável
 * (ex.: "NOME_DUPLICADO") consumido pelo frontend, e a mensagem exibível.
 *
 * <p>Lance diretamente no service quando uma regra for violada:
 * <pre>{@code
 * throw new BusinessException(HttpStatus.CONFLICT, "NOME_DUPLICADO",
 *         "Já existe um item com esse nome");
 * }</pre>
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String codigo;

    public BusinessException(HttpStatus status, String codigo, String message) {
        super(message);
        this.status = status;
        this.codigo = codigo;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCodigo() {
        return codigo;
    }
}