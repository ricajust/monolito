package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.AlunoInfoDTO;
import com.edugo.edugo_tcc.dto.MatriculaDetalheDTO;
import com.edugo.edugo_tcc.dto.PagamentoDTO;
import com.edugo.edugo_tcc.dto.PagamentoResponseDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Cobranca;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;
import com.edugo.edugo_tcc.model.Pagamento;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.repository.CobrancaRepository;
import com.edugo.edugo_tcc.repository.MatriculaRepository;
import com.edugo.edugo_tcc.repository.PagamentoRepository;
import com.edugo.edugo_tcc.service.PagamentoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private CobrancaRepository cobrancaRepository;

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
 
    /**
     * Método responsável por gerar um pagamento para um aluno. Se um pagamento pendente for encontrado, 
     * seu valor total é atualizado com o valor das novas disciplinas. A data de vencimento permanece 
     * a mesma do pagamento pendente. 
     *
     * @param alunoId
     * @return PagamentoDTO
     */
    @Transactional
    public PagamentoResponseDTO gerarPagamentoParaAluno(UUID alunoId) {
        // Busca o aluno
        Aluno aluno = alunoRepository
            .findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));
    
        // Busca um pagamento pendente existente para o aluno
        Pagamento pagamentoPendente = pagamentoRepository.findByStatusAndAluno("PENDENTE", aluno)
        .orElse(null);
    
        List<MatriculaDetalheDTO> detalhesMatriculas = new ArrayList<>();
        BigDecimal valorTotalNovasDisciplinas = BigDecimal.ZERO;
        LocalDate dataMatriculaMaisRecente = null;
        List<Matricula> novasMatriculasAtivas = new ArrayList<>();
    
        // Busca as matrículas ativas do aluno
        List<Matricula> matriculasAtivas = matriculaRepository.findByAlunoAndStatus(aluno, "ATIVO");
    
        if (matriculasAtivas.isEmpty() && pagamentoPendente == null) {
            return null;
        }
    
        for (Matricula matricula : matriculasAtivas) {
            Disciplina disciplina = matricula.getDisciplina();
            valorTotalNovasDisciplinas = valorTotalNovasDisciplinas.add(disciplina.getValor());
    
            MatriculaDetalheDTO detalhe = new MatriculaDetalheDTO();
            detalhe.setIdMatricula(matricula.getId());
            detalhe.setDisciplinaNome(disciplina.getNome());
            detalhe.setDisciplinaValor(disciplina.getValor());
            detalhesMatriculas.add(detalhe);
    
            if (dataMatriculaMaisRecente == null || matricula.getDataMatricula().isAfter(dataMatriculaMaisRecente)) {
                dataMatriculaMaisRecente = matricula.getDataMatricula();
            }
            novasMatriculasAtivas.add(matricula);
        }
    
        LocalDate dataVencimento = null;
        if (dataMatriculaMaisRecente != null) {
            dataVencimento = dataMatriculaMaisRecente.plusMonths(1);
        } else if (pagamentoPendente != null) {
            dataVencimento = pagamentoPendente.getDataVencimento();
        } else {
            dataVencimento = LocalDate.now().plusMonths(1);
        }
    
        Pagamento pagamentoSalvo;
        List<UUID> idsCobrancas = new ArrayList<>();
    
        if (pagamentoPendente != null) {
            // Atualiza o pagamento existente
            BigDecimal novoValorTotal = pagamentoPendente.getValorTotal().add(valorTotalNovasDisciplinas);
            pagamentoPendente.setValorTotal(novoValorTotal);
            pagamentoSalvo = pagamentoRepository.save(pagamentoPendente);
            idsCobrancas = cobrancaRepository
                            .findByPagamento(pagamentoSalvo)
                            .stream()
                            .map(Cobranca::getId)
                            .collect(Collectors.toList());
    
        } else {
            // Gera um novo pagamento
            Pagamento novoPagamento = new Pagamento();
            novoPagamento.setAluno(aluno);
            novoPagamento.setValorTotal(valorTotalNovasDisciplinas);
            novoPagamento.setDataVencimento(dataVencimento);
            novoPagamento.setStatus("PENDENTE");
            pagamentoSalvo = pagamentoRepository.save(novoPagamento);
            idsCobrancas = gerarCobrancas(pagamentoSalvo);
        }
    
        PagamentoResponseDTO response = new PagamentoResponseDTO();
        response.setId(pagamentoSalvo.getId());
        response.setValorTotal(pagamentoSalvo.getValorTotal());
        response.setDataVencimento(pagamentoSalvo.getDataVencimento());
        response.setStatus(pagamentoSalvo.getStatus());
        response.setAluno(conversorGenericoDTO.converterParaDTO(aluno, AlunoInfoDTO.class));
        response.setDetalhesMatriculas(detalhesMatriculas);
        response.setIdsCobrancas(idsCobrancas);
    
        return response;
    }

    private List<UUID> gerarCobrancas(Pagamento pagamento) {
        List<UUID> idsCobrancas = new ArrayList<>();
        BigDecimal valorTotal = pagamento.getValorTotal();
        BigDecimal valorParcela = valorTotal.divide(new BigDecimal(12), 2, java.math.RoundingMode.UP);
        LocalDate dataVencimentoBase = pagamento.getDataVencimento();

        for (int i = 0; i < 12; i++) {
            Cobranca cobranca = new Cobranca();
            cobranca.setPagamento(pagamento);
            cobranca.setDataPagamento(dataVencimentoBase.plusMonths(i));
            cobranca.setMetodoPagamento("BOLETO"); // Defina o método de pagamento conforme sua necessidade

            Cobranca cobrancaSalva = cobrancaRepository.save(cobranca);
            idsCobrancas.add(cobrancaSalva.getId());
        }
        return idsCobrancas;
    }

    /**
     * Método responsável por buscar um pagamento por ID
     *
     * @param id
     * @return PagamentoDTO
     */
    @Override
    public PagamentoResponseDTO buscarPagamentoPorId(UUID id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));
        PagamentoResponseDTO responseDTO = conversorGenericoDTO.converterParaDTO(pagamento, PagamentoResponseDTO.class);
        responseDTO.setAluno(conversorGenericoDTO.converterParaDTO(pagamento.getAluno(), AlunoInfoDTO.class));

        List<UUID> idsCobrancas = cobrancaRepository.findByPagamento(pagamento)
                .stream()
                .map(Cobranca::getId)
                .collect(Collectors.toList());
        responseDTO.setIdsCobrancas(idsCobrancas);

        List<Matricula> matriculas = matriculaRepository.findByAlunoAndStatus(pagamento.getAluno(), "ATIVO");
        List<MatriculaDetalheDTO> detalhesMatriculas = matriculas.stream()
                .map(matricula -> {
                    MatriculaDetalheDTO detalhe = new MatriculaDetalheDTO();
                    detalhe.setIdMatricula(matricula.getId());
                    detalhe.setDisciplinaNome(matricula.getDisciplina().getNome());
                    detalhe.setDisciplinaValor(matricula.getDisciplina().getValor());
                    return detalhe;
                })
                .collect(Collectors.toList());
        responseDTO.setDetalhesMatriculas(detalhesMatriculas);

        return responseDTO;
    }


    /**
     * Método responsável por buscar todos os pagamentos
     *
     * @return List<PagamentoDTO>
     */
    @Override
    public List<PagamentoResponseDTO> buscarTodosPagamentos() {
        try {
            List<Pagamento> pagamentos = pagamentoRepository.findAll();
            List<PagamentoResponseDTO> pagamentosResponseDTO = new ArrayList<>();
    
            for (Pagamento pagamento : pagamentos) {
                PagamentoResponseDTO responseDTO = converterParaPagamentoResponseDTO(pagamento);
    
                // Buscar IDs das cobranças relacionadas
                List<UUID> idsCobrancas = cobrancaRepository.findByPagamento(pagamento)
                        .stream()
                        .map(Cobranca::getId)
                        .collect(Collectors.toList());
                responseDTO.setIdsCobrancas(idsCobrancas);
    
                // Buscar detalhes das matrículas relacionadas
                List<Matricula> matriculas = matriculaRepository.findByAlunoAndStatus(pagamento.getAluno(), "ATIVO");
                List<MatriculaDetalheDTO> detalhesMatriculas = matriculas.stream()
                        .map(matricula -> {
                            MatriculaDetalheDTO detalhe = new MatriculaDetalheDTO();
                            detalhe.setIdMatricula(matricula.getId());
                            detalhe.setDisciplinaNome(matricula.getDisciplina().getNome());
                            detalhe.setDisciplinaValor(matricula.getDisciplina().getValor());
                            return detalhe;
                        })
                        .collect(Collectors.toList());
                responseDTO.setDetalhesMatriculas(detalhesMatriculas);
    
                pagamentosResponseDTO.add(responseDTO);
            }
    
            return pagamentosResponseDTO;
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

    //Métodos da Classe
    private PagamentoResponseDTO converterParaPagamentoResponseDTO(Pagamento pagamento) {
        PagamentoResponseDTO responseDTO = new PagamentoResponseDTO();
        responseDTO.setId(pagamento.getId());
        responseDTO.setValorTotal(pagamento.getValorTotal());
        responseDTO.setDataVencimento(pagamento.getDataVencimento());
        responseDTO.setStatus(pagamento.getStatus());
        responseDTO.setAluno(conversorGenericoDTO.converterParaDTO(pagamento.getAluno(), AlunoInfoDTO.class));
        return responseDTO;
    }
}