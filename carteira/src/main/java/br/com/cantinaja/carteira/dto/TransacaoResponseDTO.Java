
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
    Long id,
    TipoTransacao tipo,
    BigDecimal valor,
    LocalDateTime dataHora
) {
    public static TransacaoResponse fromEntity(TransacaoCarteira transacao) {
        return new TransacaoResponse(
            transacao.getId(),
            transacao.getTipo(),
            transacao.getValor(),
            transacao.getDataHora()
        );
    }

}


