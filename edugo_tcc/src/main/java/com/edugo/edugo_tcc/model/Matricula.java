package com.edugo.edugo_tcc.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Matricula {
    //propriedades
    public Aluno aluno;
    public Disciplina disciplina;
    public LocalDate dataMatricula;
    public String status;
}
