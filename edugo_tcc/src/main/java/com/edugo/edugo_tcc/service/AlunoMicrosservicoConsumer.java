package com.edugo.edugo_tcc.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.event.AlunoAtualizadoNoMicrosservicoEvent;
import com.edugo.edugo_tcc.event.AlunoCriadoNoMicrosservicoEvent;
import com.edugo.edugo_tcc.event.AlunoExcluidoNoMicrosservicoEvent;

@Component
public class AlunoMicrosservicoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlunoMicrosservicoConsumer.class);

    @Autowired
    private AlunoService alunoService; // Injete o seu AlunoService do monolito

    // private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // Assumindo o formato AAAA-MM-DD
    // private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    public void receberAlunoCriado(Message message, AlunoCriadoNoMicrosservicoEvent evento) {
        String messageBody = new String(message.getBody());
        logger.info("Mensagem recebida (raw): {}", messageBody);
        if (!"monolito".equals(evento.getOrigem()) && messageBody.contains("\"eventType\":\"AlunoCriado\"")) {
            logger.info("Evento AlunoCriado recebido do microsserviço: {}", evento);
            AlunoDTO alunoDTO = converterParaDTO(evento, messageBody); // Passamos o messageBody para o converter
            alunoService.criarAluno(alunoDTO);
        } else if ("monolito".equals(evento.getOrigem())) {
            logger.info("Evento AlunoCriado ignorado (origem: monolito).");
        } else {
            logger.warn("Mensagem recebida não é do tipo AlunoCriado.");
        }
    }

    // @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    // public void receberAlunoAtualizado(Message message, AlunoAtualizadoNoMicrosservicoEvent evento) {
    //     String messageBody = new String(message.getBody());
    //     logger.info("Mensagem recebida (raw): {}", messageBody);
    //     if (!"monolito".equals(evento.getOrigem()) && messageBody.contains("\"eventType\":\"AlunoAtualizado\"")) {
    //         logger.info("Evento AlunoAtualizado recebido do microsserviço: {}", evento);
    //         AlunoDTO alunoDTO = converterParaDTO(evento);
    //         alunoService.atualizarAluno(evento.getId(), alunoDTO); // Use o seu serviço para atualizar o aluno no monolito
    //     } else if ("monolito".equals(evento.getOrigem())) {
    //         logger.info("Evento AlunoAtualizado ignorado (origem: monolito).");
    //     } else {
    //         logger.warn("Mensagem recebida não é do tipo AlunoAtualizado.");
    //     }
    // }

    // @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    // public void receberAlunoExcluido(Message message, AlunoExcluidoNoMicrosservicoEvent evento) {
    //     String messageBody = new String(message.getBody());
    //     logger.info("Mensagem recebida (raw): {}", messageBody);
    //     if (!"monolito".equals(evento.getOrigem()) && messageBody.contains("\"eventType\":\"AlunoExcluido\"")) {
    //         logger.info("Evento AlunoExcluido recebido do microsserviço: {}", evento);
    //         alunoService.excluirAluno(evento.getId()); // Use o seu serviço para excluir o aluno no monolito
    //     } else if ("monolito".equals(evento.getOrigem())) {
    //         logger.info("Evento AlunoExcluido ignorado (origem: monolito).");
    //     } else {
    //         logger.warn("Mensagem recebida não é do tipo AlunoExcluido.");
    //     }
    // }

    // Método auxiliar para converter AlunoCriadoNoMicrosservicoEvent para AlunoDTO
    private AlunoDTO converterParaDTO(AlunoCriadoNoMicrosservicoEvent evento, String messageBody) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setCpf(evento.getCpf());
        dto.setEmail(evento.getEmail());
        dto.setTelefone(evento.getTelefone());
        dto.setEndereco(evento.getEndereco());
        dto.setBairro(evento.getBairro());
        dto.setCidade(evento.getCidade());
        dto.setUf(evento.getUf());
        dto.setCep(evento.getCep());
        dto.setSenha(evento.getSenha());

        try {
            com.fasterxml.jackson.databind.JsonNode rootNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(messageBody);
            String dataNascimentoStr = rootNode.path("dataNascimento").asText();
            if (!dataNascimentoStr.isEmpty()) {
                LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
                dto.setDataNascimento(dataNascimento);
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Erro ao processar JSON para data de nascimento: {}", e.getMessage());
        }

        return dto;
    }

    // Método auxiliar para converter AlunoAtualizadoNoMicrosservicoEvent para AlunoDTO
    private AlunoDTO converterParaDTO(AlunoAtualizadoNoMicrosservicoEvent evento) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setCpf(evento.getCpf());
        if (evento.getDataNascimento() != null && !evento.getDataNascimento().isEmpty()) {
            LocalDate dataNascimento = LocalDate.parse(evento.getDataNascimento(), dateFormatter);
            dto.setDataNascimento(dataNascimento);
        }
        dto.setEmail(evento.getEmail());
        dto.setTelefone(evento.getTelefone());
        dto.setEndereco(evento.getEndereco());
        dto.setBairro(evento.getBairro());
        dto.setCidade(evento.getCidade());
        dto.setUf(evento.getUf());
        dto.setCep(evento.getCep());
        return dto;
    }
}