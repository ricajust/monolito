package com.edugo.edugo_tcc.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoCriadoNoMicrosservicoEvent {
    private UUID id;
    private String nome;
    private String cpf;
    //@JsonFormat(pattern = "yyyy-MM-dd") // Adicione esta anotação
    private String dataNascimento;
    private String email;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String senha;
    private String origem;
    private String eventType;
}