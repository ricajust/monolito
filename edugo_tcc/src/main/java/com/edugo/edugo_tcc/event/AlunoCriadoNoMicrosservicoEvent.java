package com.edugo.edugo_tcc.event;

import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoCriadoNoMicrosservicoEvent {
    private UUID id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String email;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String senha;
}
