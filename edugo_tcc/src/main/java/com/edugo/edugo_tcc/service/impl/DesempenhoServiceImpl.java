package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.DesempenhoDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Desempenho;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.repository.DesempenhoRepository;
import com.edugo.edugo_tcc.repository.DisciplinaRepository;
import com.edugo.edugo_tcc.service.DesempenhoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DesempenhoServiceImpl implements DesempenhoService {

    private final DesempenhoRepository desempenhoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public DesempenhoServiceImpl(
        DesempenhoRepository desempenhoRepository, 
        AlunoRepository alunoRepository, 
        DisciplinaRepository disciplinaRepository, 
        ConversorGenericoDTO conversorGenericoDTO, 
        ConversorGenericoEntidade conversorGenericoEntidade) 
        {
            this.desempenhoRepository = desempenhoRepository;
            this.alunoRepository = alunoRepository;
            this.disciplinaRepository = disciplinaRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
            this.conversorGenericoEntidade = conversorGenericoEntidade;
        }

    /**
     * Método responsável por criar um desempenho
     * 
     * @param desempenhoDTO
     * @return DesempenhoDTO
     */
    @Override
    public DesempenhoDTO criarDesempenho(DesempenhoDTO desempenhoDTO) {
        try {
            Desempenho desempenho = conversorGenericoEntidade.converterParaEntidade(desempenhoDTO, Desempenho.class);
    
            Aluno aluno = alunoRepository.findById(desempenhoDTO.getAluno().getId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + desempenhoDTO.getAluno().getId()));
            Disciplina disciplina = disciplinaRepository.findById(desempenhoDTO.getDisciplina().getId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + desempenhoDTO.getDisciplina().getId()));
    
            desempenho.setAluno(aluno);
            desempenho.setDisciplina(disciplina);
    
            Desempenho desempenhoSalvo = desempenhoRepository.save(desempenho);
            return conversorGenericoDTO.converterParaDTO(desempenhoSalvo, DesempenhoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar desempenho: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar um desempenho por ID
     * 
     * @param id
     * @return DesempenhoDTO
     */
    @Override
    public DesempenhoDTO buscarDesempenhoPorId(UUID id) {
        try {
            Desempenho desempenho = desempenhoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Desempenho não encontrado com ID: " + id));
            return conversorGenericoDTO.converterParaDTO(desempenho, DesempenhoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar desempenho por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsavel por buscar todos os desempenhos
     * 
     * @return List<DesempenhoDTO>
     */
    @Override
    public List<DesempenhoDTO> buscarTodosDesempenhos() {
        try {
            List<Desempenho> desempenhos = desempenhoRepository.findAll();
            return desempenhos.stream()
                    .map(desempenho -> conversorGenericoDTO.converterParaDTO(desempenho, DesempenhoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar todos os desempenhos: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar um desempenho
     * 
     * @param id
     * @param desempenhoDTO
     * @return DesempenhoDTO
     */
    @Override
    public DesempenhoDTO atualizarDesempenho(UUID id, DesempenhoDTO desempenhoDTO) {
        try {
            Desempenho desempenhoExistente = desempenhoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Desempenho não encontrado com ID: " + id));
    
            Desempenho desempenhoAtualizado = conversorGenericoEntidade.converterParaEntidade(desempenhoDTO, Desempenho.class);
            desempenhoAtualizado.setId(desempenhoExistente.getId());
            desempenhoAtualizado.setAluno(desempenhoExistente.getAluno());
            desempenhoAtualizado.setDisciplina(desempenhoExistente.getDisciplina());
            desempenhoAtualizado = desempenhoRepository.save(desempenhoAtualizado);
            return conversorGenericoDTO.converterParaDTO(desempenhoAtualizado, DesempenhoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao atualizar desempenho: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por excluir um desempenho
     * 
     * @param id
     * @return DesempenhoDTO
     */
    @Override
    public DesempenhoDTO excluirDesempenho(UUID id) {
        try {
            Desempenho desempenho = desempenhoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Desempenho não encontrado com ID: " + id));
            desempenhoRepository.delete(desempenho);
            return conversorGenericoDTO.converterParaDTO(desempenho, DesempenhoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir desempenho: " + error.getMessage(), error);
        }
    }
}

