package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DisciplinaResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String nivel;
    private BigDecimal valor;
    private ProfessorInfoDTO professor; // DTO simplificado para o professor
}