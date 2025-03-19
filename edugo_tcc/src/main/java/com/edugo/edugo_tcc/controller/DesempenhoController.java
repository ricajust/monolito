package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoInfoDTO;
import com.edugo.edugo_tcc.dto.DesempenhoDTO;
import com.edugo.edugo_tcc.dto.DesempenhoResponseDTO;
import com.edugo.edugo_tcc.dto.DisciplinaInfoDTO;
import com.edugo.edugo_tcc.service.DesempenhoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/desempenhos")
public class DesempenhoController {

    private final DesempenhoService desempenhoService;
    private final ConversorGenericoDTO conversorGenericoDTO; // Adicione esta linha

    @Autowired
    public DesempenhoController(DesempenhoService desempenhoService, ConversorGenericoDTO conversorGenericoDTO) { // Modifique o construtor
        this.desempenhoService = desempenhoService;
        this.conversorGenericoDTO = conversorGenericoDTO; // Inicialize o conversor
    }

    @PostMapping
    public ResponseEntity<DesempenhoResponseDTO> criarDesempenho(@RequestBody DesempenhoDTO desempenhoDTO) {
        DesempenhoDTO desempenhoCriado = desempenhoService.criarDesempenho(desempenhoDTO);
        DesempenhoResponseDTO responseDTO = converterParaResponseDTO(desempenhoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesempenhoResponseDTO> buscarDesempenhoPorId(@PathVariable UUID id) {
        DesempenhoDTO desempenhoDTO = desempenhoService.buscarDesempenhoPorId(id);
        if (desempenhoDTO != null) {
            DesempenhoResponseDTO responseDTO = converterParaResponseDTO(desempenhoDTO);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DesempenhoResponseDTO>> buscarTodosDesempenhos() {
        List<DesempenhoDTO> desempenhosDTO = desempenhoService.buscarTodosDesempenhos();
        List<DesempenhoResponseDTO> responseDTOS = desempenhosDTO.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesempenhoResponseDTO> atualizarDesempenho(@PathVariable UUID id, @RequestBody DesempenhoDTO desempenhoDTO) {
        DesempenhoDTO desempenhoAtualizado = desempenhoService.atualizarDesempenho(id, desempenhoDTO);
        if (desempenhoAtualizado != null) {
            DesempenhoResponseDTO responseDTO = converterParaResponseDTO(desempenhoAtualizado);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDesempenho(@PathVariable UUID id) {
        desempenhoService.excluirDesempenho(id);
        return ResponseEntity.noContent().build();
    }

    private DesempenhoResponseDTO converterParaResponseDTO(DesempenhoDTO dto) {
        DesempenhoResponseDTO responseDTO = new DesempenhoResponseDTO();
        responseDTO.setId(dto.getId());
        responseDTO.setNota(dto.getNota());
        responseDTO.setFaltas(dto.getFaltas());
        responseDTO.setAluno(conversorGenericoDTO.converterParaDTO(dto.getAluno(), AlunoInfoDTO.class)); // Use o conversor
        responseDTO.setDisciplina(conversorGenericoDTO.converterParaDTO(dto.getDisciplina(), DisciplinaInfoDTO.class)); // Use o conversor
        return responseDTO;
    }
}