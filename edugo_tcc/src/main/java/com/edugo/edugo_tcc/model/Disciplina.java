package com.edugo.edugo_tcc.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {
    //propriedades
    public List<Matricula> matricula;
    public String nome;
    public String descricao;
    public String nivel;
    public BigDecimal valor;
    public Professor professor;
}
