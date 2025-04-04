package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.AlunoDTO;

public interface AlunoService {
    AlunoDTO criarAluno(AlunoDTO alunoDTO);
    AlunoDTO buscarAlunoPorId(UUID id);
    List<AlunoDTO> buscarTodosAlunos();
    AlunoDTO atualizarAluno(UUID id, AlunoDTO alunoDTO);
    AlunoDTO excluirAluno(UUID id, String origem);
    AlunoDTO excluirAluno(UUID id);
}
