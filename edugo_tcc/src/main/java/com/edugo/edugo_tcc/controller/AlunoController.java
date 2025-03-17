package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.dto.AlunoResponseDTO;
import com.edugo.edugo_tcc.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody AlunoDTO alunoDTO) {
        AlunoDTO alunoCriado = alunoService.criarAluno(alunoDTO);
        AlunoResponseDTO alunoResponseDTO = new AlunoResponseDTO();
        // Mapeie os atributos de AlunoDTO para AlunoResponseDTO
        alunoResponseDTO.setId(alunoCriado.getId());
        alunoResponseDTO.setNome(alunoCriado.getNome());
        alunoResponseDTO.setCpf(alunoCriado.getCpf());
        alunoResponseDTO.setDataNascimento(alunoCriado.getDataNascimento());
        alunoResponseDTO.setEmail(alunoCriado.getEmail());
        alunoResponseDTO.setTelefone(alunoCriado.getTelefone());
        alunoResponseDTO.setEndereco(alunoCriado.getEndereco());
        alunoResponseDTO.setBairro(alunoCriado.getBairro());
        alunoResponseDTO.setCidade(alunoCriado.getCidade());
        alunoResponseDTO.setUf(alunoCriado.getUf());
        alunoResponseDTO.setCep(alunoCriado.getCep());
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable UUID id) {
        AlunoDTO alunoDTO = alunoService.buscarAlunoPorId(id);
        if (alunoDTO != null) {
            AlunoResponseDTO alunoResponseDTO = new AlunoResponseDTO();
            // Mapeie os atributos de AlunoDTO para AlunoResponseDTO
            alunoResponseDTO.setId(alunoDTO.getId());
            alunoResponseDTO.setNome(alunoDTO.getNome());
            alunoResponseDTO.setCpf(alunoDTO.getCpf());
            alunoResponseDTO.setDataNascimento(alunoDTO.getDataNascimento());
            alunoResponseDTO.setEmail(alunoDTO.getEmail());
            alunoResponseDTO.setTelefone(alunoDTO.getTelefone());
            alunoResponseDTO.setEndereco(alunoDTO.getEndereco());
            alunoResponseDTO.setBairro(alunoDTO.getBairro());
            alunoResponseDTO.setCidade(alunoDTO.getCidade());
            alunoResponseDTO.setUf(alunoDTO.getUf());
            alunoResponseDTO.setCep(alunoDTO.getCep());
            return ResponseEntity.ok(alunoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> buscarTodosAlunos() {
        List<AlunoDTO> alunosDTO = alunoService.buscarTodosAlunos();
        List<AlunoResponseDTO> alunoResponseDTOS = alunosDTO.stream()
            .map(alunoDTO -> {
                AlunoResponseDTO alunoResponseDTO = new AlunoResponseDTO();
                 // Mapeie os atributos de AlunoDTO para AlunoResponseDTO
                alunoResponseDTO.setId(alunoDTO.getId());
                alunoResponseDTO.setNome(alunoDTO.getNome());
                alunoResponseDTO.setCpf(alunoDTO.getCpf());
                alunoResponseDTO.setDataNascimento(alunoDTO.getDataNascimento());
                alunoResponseDTO.setEmail(alunoDTO.getEmail());
                alunoResponseDTO.setTelefone(alunoDTO.getTelefone());
                alunoResponseDTO.setEndereco(alunoDTO.getEndereco());
                alunoResponseDTO.setBairro(alunoDTO.getBairro());
                alunoResponseDTO.setCidade(alunoDTO.getCidade());
                alunoResponseDTO.setUf(alunoDTO.getUf());
                alunoResponseDTO.setCep(alunoDTO.getCep());
                return alunoResponseDTO;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(alunoResponseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@PathVariable UUID id, @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoDTO);
        if (alunoAtualizado != null) {
            AlunoResponseDTO alunoResponseDTO = new AlunoResponseDTO();
             // Mapeie os atributos de AlunoDTO para AlunoResponseDTO
            alunoResponseDTO.setId(alunoAtualizado.getId());
            alunoResponseDTO.setNome(alunoAtualizado.getNome());
            alunoResponseDTO.setCpf(alunoAtualizado.getCpf());
            alunoResponseDTO.setDataNascimento(alunoAtualizado.getDataNascimento());
            alunoResponseDTO.setEmail(alunoAtualizado.getEmail());
            alunoResponseDTO.setTelefone(alunoAtualizado.getTelefone());
            alunoResponseDTO.setEndereco(alunoAtualizado.getEndereco());
            alunoResponseDTO.setBairro(alunoAtualizado.getBairro());
            alunoResponseDTO.setCidade(alunoAtualizado.getCidade());
            alunoResponseDTO.setUf(alunoAtualizado.getUf());
            alunoResponseDTO.setCep(alunoAtualizado.getCep());
            return ResponseEntity.ok(alunoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluno(@PathVariable UUID id) {
        alunoService.excluirAluno(id);
        return ResponseEntity.noContent().build();
    }
}