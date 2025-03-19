package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DisciplinaInfoDTO {
    private Long id;
    private String nome;
    private BigDecimal valor;
}