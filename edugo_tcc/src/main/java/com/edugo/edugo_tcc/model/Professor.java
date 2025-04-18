package com.edugo.edugo_tcc.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor extends Usuario {
    //propriedades
    @NotBlank
    private String formacao;
    @NotBlank
    private String especialidade;

    //metodos
    public Desempenho lancarNotasEFaltas(Aluno aluno, Disciplina disciplina, double nota, int falta) {
        return new Desempenho();
    }
}
