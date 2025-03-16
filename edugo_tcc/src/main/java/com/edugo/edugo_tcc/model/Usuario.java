package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.*;
import com.edugo.edugo_tcc.validation.CPFValido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {

    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String nome;

    @CPFValido
    private String cpf;

    @Past
    private LocalDate dataNascimento;

    @Email
    private String email;

    @Size(min = 8, max = 20)
    private String telefone;

    @Size(max = 255)
    private String endereco;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Size(min = 2, max = 2)
    private String uf;

    @Size(min = 8, max = 9)
    private String cep;

    @NotBlank
    @Size(min = 8)
    private String senha;
}