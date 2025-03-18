package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.dto.AlunoInfoDTO;
import com.edugo.edugo_tcc.dto.DisciplinaDTO;
import com.edugo.edugo_tcc.dto.MatriculaDTO;
import com.edugo.edugo_tcc.dto.MatriculaResponseDTO;
import com.edugo.edugo_tcc.model.Matricula;
import com.edugo.edugo_tcc.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @Autowired
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /**
     * Método responsável por criar uma matricula
     *
     * @param matriculaDTO
     * @return MatriculaResponseDTO
     */
    @PostMapping
    public ResponseEntity<List<MatriculaResponseDTO>> criarMatriculas(@RequestBody Map<String, Object> requestBody) {
        MatriculaDTO matriculaDTO = new MatriculaDTO();

        // Extrair informações do aluno
        Map<String, String> alunoData = (Map<String, String>) requestBody.get("aluno");
        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setId(java.util.UUID.fromString(alunoData.get("id")));
        matriculaDTO.setAluno(alunoDTO);

        // Extrair lista de IDs de disciplinas
        Map<String, List<Integer>> disciplinaData = (Map<String, List<Integer>>) requestBody.get("disciplina");
        List<Integer> disciplinaIds = disciplinaData.get("id");
        List<DisciplinaDTO> disciplinasDTO = new ArrayList<>();
        for (Integer id : disciplinaIds) {
            DisciplinaDTO disciplina = new DisciplinaDTO();
            disciplina.setId(Long.valueOf(id));
            disciplinasDTO.add(disciplina);
        }
        matriculaDTO.setDisciplinas(disciplinasDTO);

        // Converter String para LocalDate
        String dataMatriculaStr = (String) requestBody.get("dataMatricula");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE; // Formato padrão AAAA-MM-DD
        LocalDate dataMatricula = LocalDate.parse(dataMatriculaStr, formatter);
        matriculaDTO.setDataMatricula(dataMatricula);

        matriculaDTO.setStatus((String) requestBody.get("status"));

        List<Matricula> matriculasCriadas = matriculaService.criarMatricula(matriculaDTO); // Mudando para List<Matricula>
        List<MatriculaResponseDTO> matriculasResponseDTO = matriculasCriadas.stream()
                .map(matricula -> {
                    MatriculaResponseDTO response = new MatriculaResponseDTO();
                    response.setId(matricula.getId());
                    response.setDataMatricula(matricula.getDataMatricula());
                    response.setStatus(matricula.getStatus());

                    if (matricula.getAluno() != null) {
                        AlunoInfoDTO alunoInfo = new AlunoInfoDTO();
                        alunoInfo.setId(matricula.getAluno().getId());
                        alunoInfo.setNome(matricula.getAluno().getNome());
                        response.setAluno(alunoInfo);
                    }

                    if (matricula.getDisciplina() != null) {
                        response.setDisciplinaId(matricula.getDisciplina().getId());
                        response.setDisciplinaNome(matricula.getDisciplina().getNome());
                        response.setDisciplinaValor(matricula.getDisciplina().getValor());
                        if (matricula.getDisciplina().getProfessor() != null) {
                            response.setProfessorNome(matricula.getDisciplina().getProfessor().getNome());
                        }
                    }
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculasResponseDTO);
    }
    /**
     * Método responsável por buscar uma matrícula por ID
     *
     * @param id
     * @return MatriculaDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> buscarMatriculaPorId(@PathVariable Long id) {
        MatriculaDTO matriculaDTO = matriculaService.buscarMatriculaPorId(id);
        if (matriculaDTO != null) {
            return ResponseEntity.ok(matriculaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todas as matrículas de um aluno
     *
     * @param cpf
     * @return <List<MatriculaResponseDTO>
     */
    @GetMapping("/aluno/cpf/{cpf}")
    public ResponseEntity<List<MatriculaResponseDTO>> buscarMatriculasPorCpfAluno(@PathVariable String cpf) {
        List<MatriculaDTO> matriculasDTO = matriculaService.buscarMatriculasPorCpfAluno(cpf);
        List<MatriculaResponseDTO> matriculasResponseDTO = matriculasDTO
            .stream()
            .map(this::converterParaMatriculaResponseDTO)
            .toList();
        return ResponseEntity.ok(matriculasResponseDTO);
    }

    /**
     * Método responsável por buscar todas as matrículas
     *
     * @return List<MatriculaResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> buscarTodasMatriculas() {
        List<MatriculaDTO> matriculasDTO = matriculaService.buscarTodasMatriculas();
        List<MatriculaResponseDTO> matriculasResponseDTO = matriculasDTO
            .stream()
            .map(this::converterParaMatriculaResponseDTO)
            .toList();
        return ResponseEntity.ok(matriculasResponseDTO);
    }

    /**
     * Método responsável por atualizar uma matrícula
     *
     * @param id
     * @param matriculaDTO
     * @return MatriculaDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> atualizarMatricula(@PathVariable Long id, @RequestBody MatriculaDTO matriculaDTO) {
        MatriculaDTO matriculaAtualizada = matriculaService.atualizarMatricula(id, matriculaDTO);
        if (matriculaAtualizada != null) {
            return ResponseEntity.ok(converterParaMatriculaResponseDTO(matriculaAtualizada));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por excluir uma matrícula
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMatricula(@PathVariable Long id) {
        matriculaService.excluirMatricula(id);
        return ResponseEntity.noContent().build();
    }

    //Métodos da Classe
    /**
     * Método responsável por converter o MatriculaDTO em MatriculaResponseDTO
     *
     * @param dto
     * @return MatriculaResponseDTO
     */
    private MatriculaResponseDTO converterParaMatriculaResponseDTO(MatriculaDTO dto) {
        MatriculaResponseDTO responseDTO = new MatriculaResponseDTO();
        responseDTO.setId(dto.getId());
        responseDTO.setDataMatricula(dto.getDataMatricula());
        responseDTO.setStatus(dto.getStatus());

        if (dto.getAluno() != null) {
            AlunoInfoDTO alunoInfo = new AlunoInfoDTO();
            alunoInfo.setId(dto.getAluno().getId());
            alunoInfo.setNome(dto.getAluno().getNome());
            responseDTO.setAluno(alunoInfo);
        }

        // Pegamos a informação da disciplina da lista de disciplinas dentro do DTO e mostramos a informação
        // da primeira disciplina da lista para cada objeto MatriculaResponseDTO
        if (dto.getDisciplinas() != null && !dto.getDisciplinas().isEmpty()) {
            DisciplinaDTO disciplinaDTO = dto.getDisciplinas().get(0); // Pega a primeira disciplina da lista
            responseDTO.setDisciplinaId(disciplinaDTO.getId());
            responseDTO.setDisciplinaNome(disciplinaDTO.getNome());
            responseDTO.setDisciplinaValor(disciplinaDTO.getValor());
            if (disciplinaDTO.getProfessor() != null) {
                responseDTO.setProfessorNome(disciplinaDTO.getProfessor().getNome());
            }
        }
    
        return responseDTO;
    }
}