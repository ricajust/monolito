package com.edugo.edugo_tcc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Desempenho {
    //propriedades
    public Aluno aluno;
    public Disciplina disciplina;
    public double nota;
    public int faltas;
}
