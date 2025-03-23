package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.event.AlunoAtualizadoEvent;
import com.edugo.edugo_tcc.event.AlunoCriadoEvent;
import com.edugo.edugo_tcc.event.AlunoExcluidoEvent;
import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.repository.AlunoRepository;
import com.edugo.edugo_tcc.service.AlunoService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;
    private final String alunosExchangeName = "aluno-exchange";

    @Autowired
    public AlunoServiceImpl(
        AlunoRepository alunoRepository, 
        ConversorGenericoDTO conversorGenericoDTO, 
        ConversorGenericoEntidade conversorGenericoEntidade,
        RabbitTemplate rabbitTemplate) 
        {
            this.alunoRepository = alunoRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
            this.conversorGenericoEntidade = conversorGenericoEntidade;
            this.rabbitTemplate = rabbitTemplate;
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
            aluno.setCpf(aluno.getCpf().replaceAll("[^0-9]", ""));//Remove os caracteres de "-" e "."
            Aluno alunoSalvo = alunoRepository.save(aluno);
            AlunoDTO alunoCriadoDTO = conversorGenericoDTO.converterParaDTO(alunoSalvo, AlunoDTO.class);

            // Publica o evento AlunoCriado
            AlunoCriadoEvent alunoCriadoEvent = new AlunoCriadoEvent(alunoCriadoDTO);
            rabbitTemplate.convertAndSend(alunosExchangeName, "", alunoCriadoEvent); // Rota key é ignorada em Fanout exchange
            
            return alunoCriadoDTO;
        } catch(Exception error) {
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
        } catch(Exception error) {
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
        } catch(Exception error) {
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
            alunoAtualizado.setCpf(alunoExistente.getCpf().replaceAll("[^0-9]", ""));//Remove os caracteres de "-" e "."
            
            // Atualizar a senha apenas se um novo valor for fornecido
            if(alunoDTO.getSenha() != null && !alunoDTO.getSenha().isEmpty()) {
                alunoAtualizado.setSenha(alunoDTO.getSenha());
            } else {
                alunoAtualizado.setSenha(alunoExistente.getSenha()); // Manter a senha existente
            }

            alunoAtualizado = alunoRepository.save(alunoAtualizado);
            AlunoDTO alunoAtualizadoDTO = conversorGenericoDTO.converterParaDTO(alunoAtualizado, AlunoDTO.class);

            // Publica o evento AlunoAtualizado
            AlunoAtualizadoEvent alunoAtualizadoEvent = new AlunoAtualizadoEvent(alunoAtualizadoDTO);
            rabbitTemplate.convertAndSend(alunosExchangeName, "", alunoAtualizadoEvent);

            return alunoAtualizadoDTO;
        } catch(Exception error) {
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
            alunoRepository.delete(aluno);
            AlunoDTO alunoExcluidoDTO = conversorGenericoDTO.converterParaDTO(aluno, AlunoDTO.class);
            
            // Publica o evento AlunoExcluido
            AlunoExcluidoEvent alunoExcluidoEvent = new AlunoExcluidoEvent(id); // Usamos o ID para o evento de exclusão
            rabbitTemplate.convertAndSend(alunosExchangeName, "", alunoExcluidoEvent);

            return alunoExcluidoDTO;
        } catch(Exception error) {
            throw new RuntimeException("Erro ao excluir o aluno com ID " + id + ": " + error.getMessage(), error);
        }
    }
}