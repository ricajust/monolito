package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.ProfessorDTO;

public interface ProfessorService {
    ProfessorDTO criarProfessor(ProfessorDTO professorDTO);
    ProfessorDTO buscarProfessorPorId(UUID id);
    List<ProfessorDTO> buscarTodosProfessores();
    ProfessorDTO atualizarProfessor(UUID id, ProfessorDTO professorDTO);
    ProfessorDTO excluirProfessor(UUID id);
}
