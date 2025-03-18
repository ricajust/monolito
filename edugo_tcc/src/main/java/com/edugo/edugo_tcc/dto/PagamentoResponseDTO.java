package com.edugo.edugo_tcc.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PagamentoResponseDTO {
    private UUID id;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
    private AlunoInfoDTO aluno;
}