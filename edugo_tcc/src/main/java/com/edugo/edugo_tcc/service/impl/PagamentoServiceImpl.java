package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.PagamentoDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;
import com.edugo.edugo_tcc.model.Pagamento;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.repository.MatriculaRepository;
import com.edugo.edugo_tcc.repository.PagamentoRepository;
import com.edugo.edugo_tcc.service.PagamentoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public PagamentoServiceImpl(
            PagamentoRepository pagamentoRepository,
            AlunoRepository alunoRepository,
            MatriculaRepository matriculaRepository,
            ConversorGenericoDTO conversorGenericoDTO,
            ConversorGenericoEntidade conversorGenericoEntidade) {
        this.pagamentoRepository = pagamentoRepository;
        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
        this.conversorGenericoDTO = conversorGenericoDTO;
        this.conversorGenericoEntidade = conversorGenericoEntidade;
    }

    // /**
    //  * Método responsável por criar um pagamento
    //  *
    //  * @param pagamentoDTO
    //  * @return PagamentoDTO
    //  */
    // @Override
    // public PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO) {
    //     try {
    //         Pagamento pagamento = conversorGenericoEntidade.converterParaEntidade(pagamentoDTO, Pagamento.class);

    //         Aluno aluno = alunoRepository
    //                 .findById(pagamentoDTO.getAluno().getId())
    //                 .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + pagamentoDTO.getAluno().getId()));

    //         pagamento.setAluno(aluno);
    //         Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

    //         return conversorGenericoDTO.converterParaDTO(pagamentoSalvo, PagamentoDTO.class);
    //     } catch (Exception error) {
    //         throw new RuntimeException("Erro ao criar pagamento: " + error.getMessage(), error);
    //     }
    // }

    /**
     * Método responsável por gerar um pagamento para um aluno
     *
     * @param alunoId
     * @return PagamentoDTO
     */
    @Transactional
    public PagamentoDTO gerarPagamentoParaAluno(UUID alunoId) {
        //Busca o aluno
        Aluno aluno = alunoRepository
            .findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));

        //Busca as matriculas ativas do aluno
        List<Matricula> matriculasAtivas = matriculaRepository.findByAlunoAndStatus(aluno, "ATIVO");

        //Se não houver matriculas ATIVO retorna nulo
        if (matriculasAtivas.isEmpty()) {
            return null; 
        }

        BigDecimal valorTotal = BigDecimal.ZERO;
        LocalDate dataMatriculaMaisRecente = null;

        //Busca o valor das disciplinas matriculadas
        for (Matricula matricula : matriculasAtivas) {
            Disciplina disciplina = matricula.getDisciplina();
            valorTotal = valorTotal.add(disciplina.getValor());

            if (dataMatriculaMaisRecente == null || matricula.getDataMatricula().isAfter(dataMatriculaMaisRecente)) {
                dataMatriculaMaisRecente = matricula.getDataMatricula();
            }
        }

        //Se houver uma data de matricula adiciona 1 mês 
        LocalDate dataVencimento = null;
        if (dataMatriculaMaisRecente != null) {
            dataVencimento = dataMatriculaMaisRecente.plusMonths(1);
        } else {
            // Lógica de fallback caso não haja data de matrícula
            dataVencimento = LocalDate.now().plusMonths(1);
        }

        //Gera um novo pagamento
        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setAluno(aluno);
        novoPagamento.setValor(valorTotal);
        novoPagamento.setDataVencimento(dataVencimento);
        novoPagamento.setStatus("PENDENTE");

        Pagamento pagamentoSalvo = pagamentoRepository.save(novoPagamento);
        return conversorGenericoDTO.converterParaDTO(pagamentoSalvo, PagamentoDTO.class);
    }

    /**
     * Método responsável por buscar um pagamento por ID
     *
     * @param id
     * @return PagamentoDTO
     */
    @Override
    public PagamentoDTO buscarPagamentoPorId(UUID id) {
        try {
            Pagamento pagamento = pagamentoRepository
                .findById(id)
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
            Pagamento pagamentoExistente = pagamentoRepository
                .findById(id)
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