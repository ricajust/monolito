package com.edugo.edugo_tcc.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edugo.edugo_tcc.dto.ProfessorDTO;
import com.edugo.edugo_tcc.model.Professor;
import com.edugo.edugo_tcc.repository.ProfessorRepository;
import com.edugo.edugo_tcc.service.ProfessorService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public ProfessorServiceImpl(
        ProfessorRepository professorRepository, 
        ConversorGenericoDTO conversorGenericoDTO, 
        ConversorGenericoEntidade conversorGenericoEntidade) 
        {
            this.professorRepository = professorRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
            this.conversorGenericoEntidade = conversorGenericoEntidade;
        }

    /**
     * Método responsável por criar um professor
     * 
     * @param professorDTO
     * @return ProfessorDTO
     */
    @Override
    public ProfessorDTO criarProfessor(ProfessorDTO professorDTO) {
        try {
            Professor professor = conversorGenericoEntidade.converterParaEntidade(professorDTO, Professor.class);
            professor.setCpf(professor.getCpf().replaceAll("[^0-9]", ""));//Remove os caracteres de "-" e "."
            Professor professorSalvo = professorRepository.save(professor);
            return conversorGenericoDTO.converterParaDTO(professorSalvo, ProfessorDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao criar professor: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar um professor por ID
     * 
     * @param id
     * @return ProfessorDTO
     */
    @Override
    public ProfessorDTO buscarProfessorPorId(UUID id) {
        try {
            Professor professor = professorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com o ID: " + id));
            return conversorGenericoDTO.converterParaDTO(professor, ProfessorDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao buscar professor por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todos os professores
     * 
     * @return List<ProfessorDTO>
     */
    @Override
    public List<ProfessorDTO> buscarTodosProfessores() {
        try {
            List<Professor> professores = professorRepository.findAll();
            return professores
                .stream()
                .map(professor -> conversorGenericoDTO.converterParaDTO(professor, ProfessorDTO.class))
                .collect(Collectors.toList());
        } catch(Exception error) {
            throw new RuntimeException("Erro ao buscar todos os professores: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar um professor
     * 
     * @param id
     * @param professorDTO
     * @return ProfessorDTO
     */
    @Override
    public ProfessorDTO atualizarProfessor(UUID id, ProfessorDTO professorDTO) {
        try {
            Professor professorExistente = professorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com o ID: " + id));
            Professor professorAtualizado = conversorGenericoEntidade.converterParaEntidade(professorDTO, Professor.class);
            professorAtualizado.setId(professorExistente.getId());
            professorAtualizado.setCpf(professorExistente.getCpf().replaceAll("[^0-9]", ""));//Remove os caracteres de "-" e "."
                        
            // Atualizar a senha apenas se um novo valor for fornecido
            if(professorDTO.getSenha() != null && !professorDTO.getSenha().isEmpty()) {
                professorAtualizado.setSenha(professorDTO.getSenha());
            } else {
                professorAtualizado.setSenha(professorExistente.getSenha()); // Manter a senha existente
            }

            professorAtualizado = professorRepository.save(professorAtualizado);

            return conversorGenericoDTO.converterParaDTO(professorAtualizado, ProfessorDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao atualizar professor: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por excluir um professor
     * 
     * @param id
     * @return ProfessorDTO
     */
    @Override
    public ProfessorDTO excluirProfessor(UUID id) {
        try {
            Professor professor = professorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com o ID: " + id));
            professorRepository.delete(professor);
            return conversorGenericoDTO.converterParaDTO(professor, ProfessorDTO.class);
        }
        catch(Exception error) {
            throw new RuntimeException("Erro ao excluir professor: " + error.getMessage(), error);
        }
    }
}
