package com.edugo.edugo_tcc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno extends Usuario {
    //propriedades
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Matricula> matricula;

    //metodos
    public Matricula realizaMatricula(Disciplina disciplina){
        return new Matricula();
    }
}