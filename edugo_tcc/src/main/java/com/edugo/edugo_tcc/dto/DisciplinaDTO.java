package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DisciplinaDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String nivel;
    private BigDecimal valor;
    private ProfessorDTO professor;
}
