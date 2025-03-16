package com.edugo.edugo_tcc.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {
    //propriedades
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "nome", nullable = false)
    private String nome;

    @Size(max = 1000)
    @Column(name = "descricao")
    private String descricao;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "nivel", nullable = false)
    private String nivel;

    @NotNull
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;
}
