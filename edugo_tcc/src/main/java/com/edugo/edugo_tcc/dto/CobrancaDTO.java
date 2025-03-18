package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class CobrancaDTO {
    private UUID id;
    private PagamentoDTO pagamento;
    private LocalDate dataPagamento;
    private String metodoPagamento;
}
