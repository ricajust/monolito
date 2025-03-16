package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class UsuarioDTO {
    private UUID id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
}
