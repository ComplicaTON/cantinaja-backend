package br.com.cantinaja.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * Tratamento central de exceções de toda a API (todos os módulos herdam via
 * auto-configuration da common).
 *
 * <p>Estende {@link ResponseEntityExceptionHandler}: todas as exceções padrão
 * do Spring MVC (validação, corpo ilegível, método/mídia não suportados, rota
 * inexistente, versionamento de API etc.) já são tratadas pela superclasse com
 * o status HTTP correto.
 *
 * As respostas seguem o {@link ProblemDetail} (RFC 9457,
 * {@code application/problem+json}). Além dos campos padrão ({@code type},
 * {@code title}, {@code status}, {@code detail}), injetamos dois
 * <em>extension members</em> para o frontend em {@link #createResponseEntity}:
 * <ul>
 *   <li>{@code erro}: código estável e legível por máquina (ex.: NOME_DUPLICADO);</li>
 *   <li>{@code mensagem}: espelha {@code detail}, texto exibível ao usuário.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Regra de negócio explícita lançada pelos services. */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex) {
        return problem(ex.getStatus(), ex.getCodigo(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Erro não tratado processando a requisição", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "ERRO_INTERNO", "Erro interno inesperado");
    }

    /**
     * Falha de validação de @Valid em @RequestBody (DTOs). Sobrescrevemos apenas
     * para manter o detalhe amigável, campo a campo, em vez do texto genérico.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String detalhe = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatarErroCampo)
                .collect(Collectors.joining("; "));
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalhe);
        return handleExceptionInternal(ex, pd, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Rota inexistente. Sobrescrevemos para devolver uma mensagem em português
     * e não expor o caminho solicitado no corpo do erro.
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Recurso não encontrado");
        return handleExceptionInternal(ex, pd, headers, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Ponto único por onde passam TODAS as respostas de erro tratadas pela
     * superclasse. Aqui anexamos os extension members {@code erro}/{@code mensagem}.
     */
    @Override
    protected ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        if (body instanceof ProblemDetail pd) {
            pd.setProperty("erro", errosFramework(statusCode));
            pd.setProperty("mensagem", pd.getDetail());
        }
        return super.createResponseEntity(body, headers, statusCode, request);
    }


    private String errosFramework(HttpStatusCode status) {
        return switch (status.value()) {
            case 404 -> "ROTA_NAO_ENCONTRADA";
            case 405 -> "METODO_NAO_SUPORTADO";
            case 415 -> "MIDIA_NAO_SUPORTADA";
            case 406 -> "MIDIA_NAO_ACEITAVEL";
            default -> status.is4xxClientError() ? "DADO_INVALIDO" : "ERRO_INTERNO";
        };
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
