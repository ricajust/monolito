package com.edugo.edugo_tcc.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edugo.edugo_tcc.dto.DisciplinaDTO;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.repository.DisciplinaRepository;
import com.edugo.edugo_tcc.service.DisciplinaService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;

@Service
public class DisciplinaServiceImpl implements DisciplinaService{

    private final DisciplinaRepository disciplinaRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public DisciplinaServiceImpl(
            DisciplinaRepository disciplinaRepository,
            ConversorGenericoDTO conversorGenericoDTO,
            ConversorGenericoEntidade conversorGenericoEntidade) 
            {
                this.disciplinaRepository = disciplinaRepository;
                this.conversorGenericoDTO = conversorGenericoDTO;
                this.conversorGenericoEntidade = conversorGenericoEntidade;
            }

    /**
     * Método responsável por criar uma disciplina
     * 
     * @param disciplinaDTO
     * @return DisciplinaDTO
     */
    @Override
    public DisciplinaDTO criarDisciplina(DisciplinaDTO disciplinaDTO) {
        try {
            Disciplina disciplina = conversorGenericoEntidade.converterParaEntidade(disciplinaDTO, Disciplina.class);
            System.out.println("Entidade Disciplina após conversão: " + disciplina.toString()); // Adicionado log
            Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
            return conversorGenericoDTO.converterParaDTO(disciplinaSalva, DisciplinaDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao criar disciplina: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar uma disciplina por ID
     * 
     * @param id
     * @return DisciplinaDTO
     */
    @Override
    public DisciplinaDTO buscarDisciplinaPorId(Long id){
        try {
            Disciplina disciplina = disciplinaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com o ID: " + id));
            return conversorGenericoDTO.converterParaDTO(disciplina, DisciplinaDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao buscar disciplina por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todas as disciplinas
     * 
     * @return List<DisciplinaDTO>
     */
    @Override
    public List<DisciplinaDTO> buscarTodasDisciplinas(){
        try {
            List<Disciplina> disciplinas = disciplinaRepository.findAll();
            return disciplinas
                .stream()
                .map(disciplina -> conversorGenericoDTO.converterParaDTO(disciplina, DisciplinaDTO.class))
                .collect(Collectors.toList());
        } catch(Exception error) {
            throw new RuntimeException("Erro ao buscar todas as disciplinas: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar uma disciplina
     * 
     * @param id
     * @param disciplinaDTO
     * @return DisciplinaDTO
     */
    @Override
    public DisciplinaDTO atualizarDisciplina(Long id, DisciplinaDTO disciplinaDTO) {
        try {
            Disciplina disciplinaExistente = disciplinaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com o ID: " + id));
            Disciplina disciplina = conversorGenericoEntidade.converterParaEntidade(disciplinaDTO, Disciplina.class);
            disciplina.setId(disciplinaExistente.getId());
            Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
            return conversorGenericoDTO.converterParaDTO(disciplinaSalva, DisciplinaDTO.class);
        } catch(Exception error) {  
            throw new RuntimeException("Erro ao atualizar disciplina: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por excluir uma disciplina
     * 
     * @param id
     * @return DisciplinaDTO
     */
    @Override
    public DisciplinaDTO excluirDisciplina(Long id) {
        try {
            Disciplina disciplina = disciplinaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com o ID: " + id));
            disciplinaRepository.delete(disciplina);
            return conversorGenericoDTO.converterParaDTO(disciplina, DisciplinaDTO.class);
        }
        catch(Exception error) {
            throw new RuntimeException("Erro ao excluir disciplina: " + error.getMessage(), error);
        }
    }

}
