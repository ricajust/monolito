package com.edugo.edugo_tcc.service;

import com.edugo.edugo_tcc.dto.PagamentoDTO;
import com.edugo.edugo_tcc.dto.PagamentoResponseDTO;
import java.util.List;
import java.util.UUID;

public interface PagamentoService {
    PagamentoResponseDTO gerarPagamentoParaAluno(UUID alunoId);
    PagamentoResponseDTO buscarPagamentoPorId(UUID id);
    List<PagamentoResponseDTO> buscarTodosPagamentos();
    PagamentoDTO atualizarPagamento(UUID id, PagamentoDTO pagamentoDTO);
    PagamentoDTO excluirPagamento(UUID id);
}