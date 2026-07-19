package br.com.cantinaja.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

/**
 * Tratamento central de exceções de toda a API (todos os módulos herdam via
 * auto-configuration da common).
 *
 * <p>As respostas de erro seguem o {@link ProblemDetail} (RFC 9457,
 * {@code application/problem+json}) — padrão nativo do Spring. Além dos campos
 * padrão ({@code type}, {@code title}, {@code status}, {@code detail}),
 * anexamos dois <em>extension members</em> para o frontend:
 * <ul>
 *   <li>{@code erro}: código estável e legível por máquina (ex.: NOME_DUPLICADO);</li>
 *   <li>{@code mensagem}: espelha {@code detail}, texto exibível ao usuário.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Regra de negócio explícita lançada pelos services. */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex) {
        return problem(ex.getStatus(), ex.getCodigo(), ex.getMessage());
    }

    /** Falha de validação de @Valid em @RequestBody (DTOs). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(MethodArgumentNotValidException ex) {
        String detalhe = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatarErroCampo)
                .collect(Collectors.joining("; "));
        return problem(HttpStatus.BAD_REQUEST, "DADO_INVALIDO", detalhe);
    }

    /** Falha de validação em @RequestParam/@PathVariable. */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleParamValidation(HandlerMethodValidationException ex) {
        return problem(HttpStatus.BAD_REQUEST, "DADO_INVALIDO", "Parâmetros inválidos na requisição");
    }

    /** Corpo JSON ausente, malformado ou com tipo incompatível. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        return problem(HttpStatus.BAD_REQUEST, "DADO_INVALIDO", "Corpo da requisição inválido ou ausente");
    }

    /** Método HTTP não suportado pela rota (ex.: PUT numa rota só de POST). */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return problem(HttpStatus.METHOD_NOT_ALLOWED, "METODO_NAO_SUPORTADO", ex.getMessage());
    }

    /** Qualquer erro não previsto — nunca vaza stack trace para o cliente. */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "ERRO_INTERNO", "Erro interno inesperado");
    }

    private ProblemDetail problem(HttpStatus status, String codigo, String detalhe) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detalhe);
        problem.setProperty("erro", codigo);
        problem.setProperty("mensagem", detalhe);
        return problem;
    }

    private String formatarErroCampo(FieldError erro) {
        return erro.getField() + ": " + erro.getDefaultMessage();
    }
}