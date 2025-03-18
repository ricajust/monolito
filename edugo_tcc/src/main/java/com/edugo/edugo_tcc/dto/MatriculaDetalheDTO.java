package com.edugo.edugo_tcc.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MatriculaDetalheDTO {
    private Long idMatricula;
    private String disciplinaNome;
    private BigDecimal disciplinaValor;
}