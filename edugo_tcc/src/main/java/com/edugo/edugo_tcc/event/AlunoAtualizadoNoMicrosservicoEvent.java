package com.edugo.edugo_tcc.event;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoAtualizadoNoMicrosservicoEvent {
    @JsonProperty("Id")
    private UUID id;
    @JsonProperty("Nome")
    private String nome;
    @JsonProperty("Cpf")
    private String cpf;
    @JsonProperty("DataNascimento")
    private String dataNascimento;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Telefone")
    private String telefone;
    @JsonProperty("Endereco")
    private String endereco;
    @JsonProperty("Bairro")
    private String bairro;
    @JsonProperty("Cidade")
    private String cidade;
    @JsonProperty("Uf")
    private String uf;
    @JsonProperty("Cep")
    private String cep;
    @JsonProperty("Senha")
    private String senha;
    @JsonProperty("Origem")
    private String origem;
    @JsonProperty("EventType")
    private String eventType;
}