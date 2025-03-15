package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    public UUID id;
    public String nome;
    public String cpf;
    public LocalDate dataNascimento;
    public String email;
    public String telefone;
    public String endereco;
    public String bairro;
    public String cidade;
    public String uf;
    public String cep;
    public String senha;
}
