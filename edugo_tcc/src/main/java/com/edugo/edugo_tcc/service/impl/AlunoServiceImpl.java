package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.dto.DisciplinaDTO;
import com.edugo.edugo_tcc.dto.MatriculaDTO;
import com.edugo.edugo_tcc.dto.ProfessorDTO;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;
import com.edugo.edugo_tcc.model.Professor;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.service.AlunoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository alunoRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;


    @Autowired
    public AlunoServiceImpl(AlunoRepository alunoRepository, ConversorGenericoDTO conversorGenericoDTO, ConversorGenericoEntidade conversorGenericoEntidade) {
        this.alunoRepository = alunoRepository;
        this.conversorGenericoDTO = conversorGenericoDTO;
        this.conversorGenericoEntidade = conversorGenericoEntidade;
    }

    /**
     * Método responsável por criar um aluno
     * 
     * @param alunoDTO
     * @return AlunoDTO
     */
    @Override
    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        try {
            Aluno aluno = conversorGenericoEntidade.converterParaEntidade(alunoDTO, Aluno.class);
            System.out.println("Entidade Aluno após conversão: " + aluno.toString()); // Adicionado log
            Aluno alunoSalvo = alunoRepository.save(aluno);
            return conversorGenericoDTO.converterParaDTO(alunoSalvo, AlunoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar aluno: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar um aluno por ID
     * 
     * @param id
     * @return AlunoDTO
     */
    @Override
    public AlunoDTO buscarAlunoPorId(UUID id) {
        try {
            Aluno aluno = alunoRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
            return conversorGenericoDTO.converterParaDTO(aluno, AlunoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar aluno por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todos os alunos
     * 
     * @return List<AlunoDTO>
     */
    @Override
    public List<AlunoDTO> buscarTodosAlunos() {
        try {
            List<Aluno> alunos = alunoRepository.findAll();
            return alunos
                    .stream()
                    .map(aluno -> conversorGenericoDTO.converterParaDTO(aluno, AlunoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar todos os alunos: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar um aluno
     * 
     * @param id
     * @param alunoDTO
     * @return AlunoDTO
     */
    @Override
    public AlunoDTO atualizarAluno(UUID id, AlunoDTO alunoDTO) {
        try {
            Aluno alunoExistente = alunoRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
            Aluno alunoAtualizado = conversorGenericoEntidade.converterParaEntidade(alunoDTO, Aluno.class);
            alunoAtualizado.setId(alunoExistente.getId()); // Garante que o ID seja mantido
            alunoAtualizado = alunoRepository.save(alunoAtualizado);
            return conversorGenericoDTO.converterParaDTO(alunoAtualizado, AlunoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao atualizar aluno: " + error.getMessage(), error);

        }
    }

    /**
     * Método responsável por excluir um aluno
     * 
     * @param id
     * @return AlunoDTO
     */
    @Override
    public AlunoDTO excluirAluno(UUID id) {
        try {
            Aluno aluno = alunoRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
            alunoRepository.deleteById(id);
            return conversorGenericoDTO.converterParaDTO(aluno, AlunoDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir o aluno com ID " + id + ": " + error.getMessage(), error);
        }
    }

    // Metodos auxiliares
    /**
     * Método responsável por converter um aluno para DTO
     * @param aluno
     * @return AlunoDTO
     */
    private AlunoDTO converterParaDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setCpf(aluno.getCpf());
        dto.setDataNascimento(aluno.getDataNascimento());
        dto.setEmail(aluno.getEmail());
        dto.setTelefone(aluno.getTelefone());
        dto.setEndereco(aluno.getEndereco());
        dto.setBairro(aluno.getBairro());
        dto.setCidade(aluno.getCidade());
        dto.setUf(aluno.getUf());
        dto.setCep(aluno.getCep());
        dto.setMatricula(converterListaMatriculasParaDTO(aluno.getMatricula()));
        return dto;
    }

    /**
     * Método responsável por converter um alunoDTO para entidade
     * @param alunoDTO
     * @return Aluno
     */
    private Aluno converterParaEntidade(AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno();
        aluno.setId(alunoDTO.getId());
        aluno.setNome(alunoDTO.getNome());
        aluno.setCpf(alunoDTO.getCpf());
        aluno.setDataNascimento(alunoDTO.getDataNascimento());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setTelefone(alunoDTO.getTelefone());
        aluno.setEndereco(alunoDTO.getEndereco());
        aluno.setBairro(alunoDTO.getBairro());
        aluno.setCidade(alunoDTO.getCidade());
        aluno.setUf(alunoDTO.getUf());
        aluno.setCep(alunoDTO.getCep());
        aluno.setMatricula(converterListaMatriculasParaEntidade(alunoDTO.getMatricula()));
        return aluno;
    }

    /**
     * Método responsável por converter uma disciplina para DTO
     * @param disciplina
     * @return DisciplinaDTO
     */
    private DisciplinaDTO converterParaDisciplinaDTO(Disciplina disciplina) {
        if (disciplina == null) return null;
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setDescricao(disciplina.getDescricao());
        dto.setNivel(disciplina.getNivel());
        dto.setValor(disciplina.getValor());
        dto.setProfessor(converterParaProfessorDTO(disciplina.getProfessor())); // Adicionado
        return dto;
    }

    /**
     * Método responsável por converter um professor para DTO
     * @param professor
     * @return ProfessorDTO
     */
    private ProfessorDTO converterParaProfessorDTO(Professor professor) {
        if (professor == null) return null;
        ProfessorDTO dto = new ProfessorDTO();
        // Mapeie os atributos de Professor para ProfessorDTO
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setCpf(professor.getCpf());
        dto.setDataNascimento(professor.getDataNascimento());
        dto.setEmail(professor.getEmail());
        dto.setTelefone(professor.getTelefone());
        dto.setEndereco(professor.getEndereco());
        dto.setBairro(professor.getBairro());
        dto.setCidade(professor.getCidade());
        dto.setUf(professor.getUf());
        dto.setCep(professor.getCep());
        dto.setFormacao(professor.getFormacao());
        dto.setEspecialidade(professor.getEspecialidade());
        return dto;
    }

    /**
     * Método responsável por converter uma disciplinaDTO para entidade
     * @param disciplinaDTO
     * @return Disciplina
     */
    private Disciplina converterParaDisciplinaEntidade(DisciplinaDTO disciplinaDTO) {
        if (disciplinaDTO == null) return null;
        Disciplina disciplina = new Disciplina();
        // Mapeie os atributos de DisciplinaDTO para Disciplina
        disciplina.setId(disciplinaDTO.getId());
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setDescricao(disciplinaDTO.getDescricao());
        disciplina.setNivel(disciplinaDTO.getNivel());
        disciplina.setValor(disciplinaDTO.getValor());
        disciplina.setProfessor(converterParaProfessorEntidade(disciplinaDTO.getProfessor())); // Adicionado
        return disciplina;
    }

    /**
     * Método responsável por converter um professorDTO para entidade
     * @param professorDTO
     * @return Professor
     */
    private Professor converterParaProfessorEntidade(ProfessorDTO professorDTO) {
        if (professorDTO == null) return null;
        Professor professor = new Professor();
        // Mapeie os atributos de Professor para Entidade Professor
        professor.setId(professorDTO.getId());
        professor.setNome(professorDTO.getNome());
        professor.setCpf(professorDTO.getCpf());
        professor.setDataNascimento(professorDTO.getDataNascimento());
        professor.setEmail(professorDTO.getEmail());
        professor.setTelefone(professorDTO.getTelefone());
        professor.setEndereco(professorDTO.getEndereco());
        professor.setBairro(professorDTO.getBairro());
        professor.setCidade(professorDTO.getCidade());
        professor.setUf(professorDTO.getUf());
        professor.setCep(professorDTO.getCep());
        professor.setFormacao(professorDTO.getFormacao());
        professor.setEspecialidade(professorDTO.getEspecialidade());
        return professor;
    }

    /**
     * Método responsável por converter uma lista de matriculas para DTO
     * @param matriculas
     * @return List<MatriculaDTO>
     */
    private List<MatriculaDTO> converterListaMatriculasParaDTO(List<Matricula> matriculas) {
        if (matriculas == null) return null;
        return matriculas.stream()
                .map(matricula -> {
                    MatriculaDTO dto = new MatriculaDTO();
                    dto.setId(matricula.getId());
                    if(matricula.getAluno() != null) {
                        dto.setAluno(converterParaDTO(matricula.getAluno()));
                    }
                    if(matricula.getDisciplina() != null) {
                        dto.setDisciplina(converterParaDisciplinaDTO(matricula.getDisciplina()));
                    }
                    dto.setDataMatricula(matricula.getDataMatricula());
                    dto.setStatus(matricula.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Método responsável por converter uma lista de matriculas para entidade
     * @param matriculas
     * @return List<Matricula>
     */
    private List<Matricula> converterListaMatriculasParaEntidade(List<MatriculaDTO> matriculas) {
        if (matriculas == null) return null;
        return matriculas.stream()
                .map(matriculaDTO -> {
                    Matricula matricula = new Matricula();
                    matricula.setId(matriculaDTO.getId());
                    if(matriculaDTO.getAluno() != null) {
                        matricula.setAluno(converterParaEntidade(matriculaDTO.getAluno()));
                    }
                    if(matriculaDTO.getDisciplina() != null) {
                        matricula.setDisciplina(converterParaDisciplinaEntidade(matriculaDTO.getDisciplina()));
                    }
                    matricula.setDataMatricula(matriculaDTO.getDataMatricula());
                    matricula.setStatus(matriculaDTO.getStatus());
                    return matricula;
                })
                .collect(Collectors.toList());
    }
}
