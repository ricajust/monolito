package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.PagamentoDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Pagamento;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.repository.PagamentoRepository;
import com.edugo.edugo_tcc.service.PagamentoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public PagamentoServiceImpl(
        PagamentoRepository pagamentoRepository, 
        AlunoRepository alunoRepository, 
        ConversorGenericoDTO conversorGenericoDTO, 
        ConversorGenericoEntidade conversorGenericoEntidade) 
        {
            this.pagamentoRepository = pagamentoRepository;
            this.alunoRepository = alunoRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
            this.conversorGenericoEntidade = conversorGenericoEntidade;
        }

    /**
     * Método responsável por criar um pagamento
     * 
     * @param pagamentoDTO
     * @return PagamentoDTO
     */
    @Override
    public PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO) {
        try {
            Pagamento pagamento = conversorGenericoEntidade.converterParaEntidade(pagamentoDTO, Pagamento.class);

            Aluno aluno = alunoRepository.findById(pagamentoDTO.getAluno().getId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + pagamentoDTO.getAluno().getId()));

            pagamento.setAluno(aluno);

            Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);
            return conversorGenericoDTO.converterParaDTO(pagamentoSalvo, PagamentoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar pagamento: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsáve por buscar um pagamento por ID
     * 
     * @param id
     * @return PagamentoDTO
     */
    @Override
    public PagamentoDTO buscarPagamentoPorId(UUID id) {
        try {
            Pagamento pagamento = pagamentoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));
            return conversorGenericoDTO.converterParaDTO(pagamento, PagamentoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar pagamento por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todos os pagamentos
     * 
     * @return List<PagamentoDTO>
     */
    @Override
    public List<PagamentoDTO> buscarTodosPagamentos() {
        try {
            List<Pagamento> pagamentos = pagamentoRepository.findAll();
            return pagamentos.stream()
                    .map(pagamento -> conversorGenericoDTO.converterParaDTO(pagamento, PagamentoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar todos os pagamentos: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar um pagamento
     * 
     * @param id
     * @param pagamentoDTO
     * @return PagamentoDTO
     */
    @Override
    public PagamentoDTO atualizarPagamento(UUID id, PagamentoDTO pagamentoDTO) {
        try {
            Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));

            Pagamento pagamentoAtualizado = conversorGenericoEntidade.converterParaEntidade(pagamentoDTO, Pagamento.class);
            pagamentoAtualizado.setId(pagamentoExistente.getId());
            pagamentoAtualizado.setAluno(pagamentoExistente.getAluno());
            pagamentoAtualizado = pagamentoRepository.save(pagamentoAtualizado);
            return conversorGenericoDTO.converterParaDTO(pagamentoAtualizado, PagamentoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao atualizar pagamento: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por excluir um pagamento
     * 
     * @param id
     */
    @Override
    public PagamentoDTO excluirPagamento(UUID id) {
        try {
            Pagamento pagamento = pagamentoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));
            pagamentoRepository.delete(pagamento);
            return conversorGenericoDTO.converterParaDTO(pagamento, PagamentoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir pagamento: " + error.getMessage(), error);
        }
    }
}