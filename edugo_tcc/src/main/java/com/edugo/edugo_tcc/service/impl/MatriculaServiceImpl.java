package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.MatriculaDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.repository.DisciplinaRepository;
import com.edugo.edugo_tcc.repository.MatriculaRepository;
import com.edugo.edugo_tcc.service.MatriculaService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public MatriculaServiceImpl(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository,
            ConversorGenericoDTO conversorGenericoDTO,
            ConversorGenericoEntidade conversorGenericoEntidade) 
            {
                this.matriculaRepository = matriculaRepository;
                this.alunoRepository = alunoRepository;
                this.disciplinaRepository = disciplinaRepository;
                this.conversorGenericoDTO = conversorGenericoDTO;
                this.conversorGenericoEntidade = conversorGenericoEntidade;
            }

    /**
     * Método responsável por criar uma matrícula
     * 
     * @param matriculaDTO
     * @return MatriculaDTO
     */
    @Override
    public MatriculaDTO criarMatricula(MatriculaDTO matriculaDTO) {
        try {
            Matricula matricula = conversorGenericoEntidade.converterParaEntidade(matriculaDTO, Matricula.class);

            // Garante que a data de matrícula seja preenchida no backend, caso não venha do frontend
            if (matricula.getDataMatricula() == null) {
                matricula.setDataMatricula(LocalDate.now());
            }

            // Busca as entidades de Aluno e Disciplina pelos IDs
            Aluno aluno = alunoRepository.findById(matriculaDTO.getAluno().getId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + matriculaDTO.getAluno().getId()));
            Disciplina disciplina = disciplinaRepository.findById(matriculaDTO.getDisciplina().getId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + matriculaDTO.getDisciplina().getId()));

            matricula.setAluno(aluno);
            matricula.setDisciplina(disciplina);

            Matricula matriculaSalva = matriculaRepository.save(matricula);
            return conversorGenericoDTO.converterParaDTO(matriculaSalva, MatriculaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar matrícula: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar uma matricula por ID
     *
     * @param id
     * @return MatriculaDTO
     */
    @Override
    public MatriculaDTO buscarMatriculaPorId(Long id) {
        try {
            Matricula matricula = matriculaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));
            return conversorGenericoDTO.converterParaDTO(matricula, MatriculaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar matrícula por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todas as matriculas
     *
     * @return List<MatriculaDTO>
     */
    @Override
    public List<MatriculaDTO> buscarTodasMatriculas() {
        try {
            List<Matricula> matriculas = matriculaRepository.findAll();
            return matriculas.stream()
                    .map(matricula -> conversorGenericoDTO.converterParaDTO(matricula, MatriculaDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar todas as matrículas: " + error.getMessage(), error);
        }
    }

    /**
     * Método reponsável por atualizar uma matrícula
     *
     * @param id
     * @param matriculaDTO
     * @return MatriculaDTO
     */
    @Override
    public MatriculaDTO atualizarMatricula(Long id, MatriculaDTO matriculaDTO) {
        try {
        Matricula matriculaExistente = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));        
            Matricula matriculaAtualizada = conversorGenericoEntidade.converterParaEntidade(matriculaDTO, Matricula.class);
            matriculaAtualizada.setId(matriculaExistente.getId()); // Mantém o ID existente
            matriculaAtualizada.setAluno(matriculaExistente.getAluno()); // Mantém o aluno existente (ou busca se necessário)
            matriculaAtualizada.setDisciplina(matriculaExistente.getDisciplina()); // Mantém a disciplina existente (ou busca se necessário)
            matriculaAtualizada = matriculaRepository.save(matriculaAtualizada);
            return conversorGenericoDTO.converterParaDTO(matriculaAtualizada, MatriculaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao atualizar matrícula: " + error.getMessage(), error);
        }

    }

    /**
     * Método responsável por excluir uma matrícula
     *
     * @param id
     * @return MatriculaDTO
     */
    @Override
    public MatriculaDTO excluirMatricula(Long id) {
        try {
            Matricula matricula = matriculaRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com ID: " + id));
            matriculaRepository.delete(matricula);
            return conversorGenericoDTO.converterParaDTO(matricula, MatriculaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir matrícula: " + error.getMessage(), error);
        }
    }
}
