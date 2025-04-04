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

import jakarta.transaction.Transactional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository alunoRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;
    private final RabbitTemplate rabbitTemplate;
    private final String alunosExchangeName = "alunos.exchange";
    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);

    @Autowired
    public AlunoServiceImpl(
            AlunoRepository alunoRepository,
            ConversorGenericoDTO conversorGenericoDTO,
            ConversorGenericoEntidade conversorGenericoEntidade,
            RabbitTemplate rabbitTemplate) {
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
            // 1. Conversão e limpeza do CPF
            Aluno aluno = conversorGenericoEntidade.converterParaEntidade(alunoDTO, Aluno.class);
            aluno.setCpf(aluno.getCpf().replaceAll("[^0-9]", "")); // Remove não numéricos

            // Gerenciar o ID com base na origem
            if ("Microsservico".equals(alunoDTO.getOrigem())) {
                // O ID já deve estar definido pelo conversor
                logger.info("ID do aluno (microsserviço): {}", aluno.getId());
            } else {
                // Para criações locais no monolito, gerar um novo UUID
                aluno.setId(UUID.randomUUID());
                logger.info("ID do aluno (monolito - antes de salvar): {}", aluno.getId());
            }

            // 2. Verifica se já existe um aluno com o mesmo CPF
            if (alunoRepository.findByCpf(aluno.getCpf()).isPresent()) {
                throw new RuntimeException("Já existe um aluno cadastrado com este CPF: " + aluno.getCpf());
            }

            logger.info("Entidade Aluno após conversão: {}", aluno);

            // 3. Persistência
            Aluno alunoSalvo = alunoRepository.save(aluno);
            logger.info("Entidade Aluno salva: {}", alunoSalvo);
            AlunoDTO alunoCriadoDTO = conversorGenericoDTO.converterParaDTO(alunoSalvo, AlunoDTO.class);
            logger.info("Aluno criado com ID: {}", alunoCriadoDTO.getId());

            // 4. Publicação do evento
            if (!"Microsservico".equals(alunoDTO.getOrigem())) {
                alunoCriadoDTO.setOrigem("Monolito");
                alunoCriadoDTO.setEventType("AlunoCriado");
                AlunoCriadoEvent alunoCriadoEvent = new AlunoCriadoEvent(alunoCriadoDTO);
                rabbitTemplate.convertAndSend("alunos.exchange", "aluno.criado", alunoCriadoEvent);
                logger.info("Evento publicado para o aluno ID: {}", alunoCriadoDTO.getId());
                logger.info("Data de nascimento do aluno ID: {}", alunoCriadoDTO.getDataNascimento());
            } else {
                logger.info("Evento de criação não publicado (origem: monolito).");
            }
            return alunoCriadoDTO;
        } catch(Exception error) {
            logger.error("Falha ao criar aluno", error);
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
            alunoAtualizado.setCpf(alunoExistente.getCpf()); // Mantém o CPF existente

            // Atualizar a senha apenas se um novo valor for fornecido
            if(alunoDTO.getSenha() != null && !alunoDTO.getSenha().isEmpty()) {
                alunoAtualizado.setSenha(alunoDTO.getSenha());
            } else {
                alunoAtualizado.setSenha(alunoExistente.getSenha()); // Manter a senha existente
            }

            alunoAtualizado = alunoRepository.save(alunoAtualizado);
            AlunoDTO alunoAtualizadoDTO = conversorGenericoDTO.converterParaDTO(alunoAtualizado, AlunoDTO.class);

            logger.info("Aluno alterado com ID: {}", alunoAtualizadoDTO.getId());

            // Publica o evento AlunoAtualizado SOMENTE se a origem não for o microsserviço
            if (!"Microsservico".equals(alunoDTO.getOrigem())) {
                alunoAtualizadoDTO.setOrigem("Monolito");
                alunoAtualizadoDTO.setEventType("AlunoAtualizado");
                AlunoAtualizadoEvent alunoAtualizadoEvent = new AlunoAtualizadoEvent(alunoAtualizadoDTO);
                rabbitTemplate.convertAndSend(alunosExchangeName, "aluno.atualizado", alunoAtualizadoEvent);
                logger.info("Evento atualizarAluno publicado para o aluno com ID: {}", alunoAtualizadoDTO.getId());
            } else {
                logger.info("Evento atualizarAluno não publicado (origem: microsserviço).");
            }

            return alunoAtualizadoDTO;
        } catch(Exception error) {
            throw new RuntimeException("Erro ao atualizar aluno: " + error.getMessage(), error);
        }
    }


    /**
     * Método responsável por excluir um aluno
     *
     * @param id
     * @param origem (Adicione este parâmetro para receber a origem do evento)
     * @return AlunoDTO
     */
    @Override
    @Transactional
    public AlunoDTO excluirAluno(UUID id, String origem) {
        try {
            Aluno aluno = alunoRepository
                .findByIdComMatriculas(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
            alunoRepository.delete(aluno);
            AlunoDTO alunoExcluidoDTO = conversorGenericoDTO.converterParaDTO(aluno, AlunoDTO.class);
            logger.info("Aluno excluido com ID: {}", alunoExcluidoDTO.getId());

            // Publica o evento AlunoExcluido SOMENTE se a origem não for o microsserviço
            if (!"Microsservico".equals(origem)) {
                alunoExcluidoDTO.setOrigem("Monolito");
                alunoExcluidoDTO.setEventType("AlunoExcluido");
                AlunoExcluidoEvent alunoExcluidoEvent = new AlunoExcluidoEvent(alunoExcluidoDTO);
                rabbitTemplate.convertAndSend(alunosExchangeName, "aluno.excluido", alunoExcluidoEvent);
                logger.info("Evento excluirAluno publicado para o aluno com ID: {}", alunoExcluidoDTO.getId());
            } else {
                logger.info("Evento excluirAluno não publicado (origem: microsserviço).");
            }

            return alunoExcluidoDTO;
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir aluno: " + error.getMessage(), error);
        }

    }

    @Override
    @Transactional // Modifique a assinatura do método na interface também
    public AlunoDTO excluirAluno(UUID id) {
        return excluirAluno(id, null); // Chamada para a nova assinatura com origem nula para exclusões locais
    }
}