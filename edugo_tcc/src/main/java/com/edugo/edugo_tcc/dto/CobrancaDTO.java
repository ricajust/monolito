package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CobrancaDTO {
    private Long id;
    private PagamentoDTO pagamento;
    private LocalDate dataPagamento;
    private String metodoPagamento;
}
