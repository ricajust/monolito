package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.DisciplinaDTO;
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

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
     * Método reponsável por verificar se já existe uma matrícula ativa para o aluno na disciplina fornecida
     * @param aluno
     * @param disciplina
     * @return boolean
     */
    @Override
    public boolean verificaMatriculaAtivaParaAlunoEDisciplina(Aluno aluno, Disciplina disciplina) {
        return matriculaRepository.existsByAlunoAndDisciplinaAndStatus(aluno, disciplina, "ATIVO");
    }

    @Transactional
    @Override
    public List<Matricula> criarMatricula(MatriculaDTO matriculaDTO) {
        List<Matricula> matriculasCriadas = new ArrayList<>();
        Aluno aluno = alunoRepository.findById(matriculaDTO.getAluno().getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + matriculaDTO.getAluno().getId()));
        LocalDate dataMatricula = matriculaDTO.getDataMatricula() == null ? LocalDate.now() : matriculaDTO.getDataMatricula();
        String status = matriculaDTO.getStatus() != null ? matriculaDTO.getStatus() : "ATIVO"; // Defina um status padrão se não for fornecido

        for (DisciplinaDTO disciplinaDTO : matriculaDTO.getDisciplinas()) {
            Disciplina disciplina = disciplinaRepository.findById(disciplinaDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada com ID: " + disciplinaDTO.getId()));

            if (verificaMatriculaAtivaParaAlunoEDisciplina(aluno, disciplina)) {
                throw new RuntimeException("Aluno já matriculado nesta disciplina: " + disciplina.getNome());
            }

            Matricula matricula = new Matricula();
            matricula.setAluno(aluno);
            matricula.setDisciplina(disciplina);
            matricula.setDataMatricula(dataMatricula);
            matricula.setStatus(status);

            Matricula matriculaSalva = matriculaRepository.save(matricula);
            matriculasCriadas.add(matriculaSalva); // Adicionando a entidade Matricula
        }
        return matriculasCriadas;
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
     * Método responsável por buscar todas as matrículas de um aluno
     *
     * @param cpf
     * @return MatriculaDTO
     */
    @Override
    public List<MatriculaDTO> buscarMatriculasPorCpfAluno(String cpf) {
        List<Matricula> matriculas = matriculaRepository.findByAlunoCpf(cpf);
        return matriculas.stream()
            .map(matricula -> conversorGenericoDTO.converterParaDTO(matricula, MatriculaDTO.class))
            .collect(Collectors.toList());
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
        Matricula matriculaExistente = matriculaRepository
                .findById(id)
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