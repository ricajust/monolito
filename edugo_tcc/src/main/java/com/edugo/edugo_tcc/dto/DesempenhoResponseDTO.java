package com.edugo.edugo_tcc.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class DesempenhoResponseDTO {
    private UUID id;
    private AlunoInfoDTO aluno;
    private DisciplinaInfoDTO disciplina;
    private Double nota;
    private int faltas;
}