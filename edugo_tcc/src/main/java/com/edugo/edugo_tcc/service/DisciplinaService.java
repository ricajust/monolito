package com.edugo.edugo_tcc.service;

import java.util.List;
import com.edugo.edugo_tcc.dto.DisciplinaDTO;

public interface DisciplinaService {
    DisciplinaDTO criarDisciplina(DisciplinaDTO disciplinaDTO);
    DisciplinaDTO buscarDisciplinaPorId(Long id);
    List<DisciplinaDTO> buscarTodasDisciplinas();
    DisciplinaDTO atualizarDisciplina(Long id, DisciplinaDTO disciplinaDTO);
    DisciplinaDTO excluirDisciplina(Long id);
}
