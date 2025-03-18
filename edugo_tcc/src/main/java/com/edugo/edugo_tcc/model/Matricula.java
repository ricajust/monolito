package com.edugo.edugo_tcc.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Matricula {
    //propriedades
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @NotNull
    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Column(name = "status")
    private String status;
}