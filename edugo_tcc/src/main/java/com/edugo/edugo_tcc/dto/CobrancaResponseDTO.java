package com.edugo.edugo_tcc.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CobrancaResponseDTO {
    private UUID id;
    private UUID idPagamento; // Ou PagamentoInfoDTO se quiser mais detalhes do pagamento
    private LocalDate dataPagamento;
    private String metodoPagamento;
}