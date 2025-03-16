package com.edugo.edugo_tcc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno extends Usuario{
    //propriedades
    public List<Matricula> matricula;

    //metodos
    public Matricula realizaMatricula(Disciplina disciplina){
        return new Matricula();
    }
}
