package com.edugo.edugo_tcc.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PagamentoResponseDTO {
    private UUID id;
    private BigDecimal valorTotal;
    private LocalDate dataVencimento;
    private String status;
    private AlunoInfoDTO aluno;
    private List<MatriculaDetalheDTO> detalhesMatriculas;
    private List<UUID> idsCobrancas; // Lista para armazenar os IDs das cobranças geradas
}