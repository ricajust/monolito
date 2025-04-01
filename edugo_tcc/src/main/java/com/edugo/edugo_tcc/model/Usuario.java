package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "nome")
    private String nome;

    @NotBlank
    @Column(name = "cpf", unique = true, nullable = false)
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números")
    private String cpf;

    @Past
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Email
    @Column(name = "email")
    private String email;

    @Size(min = 8, max = 20)
    @Column(name = "telefone")
    private String telefone;

    @Size(max = 255)
    @Column(name = "endereco")
    private String endereco;

    @Size(max = 100)
    @Column(name = "bairro")
    private String bairro;

    @Size(max = 100)
    @Column(name = "cidade")
    private String cidade;

    @Size(min = 2, max = 2)
    @Column(name = "uf")
    private String uf;

    @Size(min = 8, max = 9)
    @Column(name = "cep")
    private String cep;

    @NotBlank
    @Size(min = 3)
    @Column(name = "senha")
    private String senha;
}