package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.PagamentoDTO;

public interface PagamentoService {
    // PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO);
    PagamentoDTO gerarPagamentoParaAluno(UUID alunoId);
    PagamentoDTO buscarPagamentoPorId(UUID id);
    List<PagamentoDTO> buscarTodosPagamentos();
    PagamentoDTO atualizarPagamento(UUID id, PagamentoDTO pagamentoDTO);
    PagamentoDTO excluirPagamento(UUID id);
}