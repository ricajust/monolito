package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class PagamentoDTO {
    private UUID id;
    private AlunoDTO aluno;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
}
