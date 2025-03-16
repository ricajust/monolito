package com.edugo.edugo_tcc.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Desempenho {
    //propriedades
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "nota", nullable = false)
    private double nota;

    @Min(0)
    @Column(name = "faltas")
    private int faltas;
}
