package com.edugo.edugo_tcc.dto;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AlunoResponseDTO {
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
    // private List<MatriculaDTO> matricula;
}