package com.edugo.edugo_tcc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor {
    //propriedades
    public String formacao;
    public String especialidade;

    //metodos
    public Desempenho lancarNotasEFaltas(Aluno aluno, Disciplina disciplina, double nota, int falta) {
        return new Desempenho();
    }
}
