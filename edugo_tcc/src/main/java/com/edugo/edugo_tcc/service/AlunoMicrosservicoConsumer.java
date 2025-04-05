package com.edugo.edugo_tcc.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
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

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.alunos.criados.queue}", durable = "true"),
            exchange = @Exchange(value = "alunos.reverse.exchange", type = ExchangeTypes.DIRECT),
            key = "aluno.criado"
        )
    )
    public void receberAlunoCriado(Message message, AlunoCriadoNoMicrosservicoEvent evento) {
        String messageBody = new String(message.getBody());
        logger.info("Mensagem recebida (raw): {}", messageBody);
        logger.info("Origem do evento recebido: {}", evento.getOrigem());
        logger.info("EventType do evento recebido: {}", evento.getEventType());
        if (!"Monolito".equals(evento.getOrigem()) && "AlunoCriado".equals(evento.getEventType())) {
            logger.info("Evento AlunoCriado recebido do microsserviço: {}", evento);
            AlunoDTO alunoDTO = converterParaDTO(evento);
            alunoService.criarAluno(alunoDTO);
        } else if ("Monolito".equals(evento.getOrigem())) {
            logger.info("Evento AlunoCriado ignorado (origem: monolito).");
        } else {
            logger.warn("Mensagem recebida não é do tipo AlunoCriado.");
        }
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.alunos.atualizados.queue}", durable = "true"),
            exchange = @Exchange(value = "alunos.reverse.exchange", type = ExchangeTypes.DIRECT),
            key = "aluno.atualizado"
        )
    )
    public void receberAlunoAtualizado(Message message, AlunoAtualizadoNoMicrosservicoEvent evento) {
        String messageBody = new String(message.getBody());
        logger.info("Mensagem recebida (raw): {}", messageBody);
        logger.info("Origem do evento recebido: {}", evento.getOrigem());
        logger.info("EventType do evento recebido: {}", evento.getEventType());
        if (!"Monolito".equals(evento.getOrigem()) && "AlunoAtualizado".equals(evento.getEventType())) {
            logger.info("Evento AlunoAtualizado recebido do microsserviço: {}", evento);
            AlunoDTO alunoDTO = converterParaDTO(evento);
            alunoService.atualizarAluno(evento.getId(), alunoDTO); // <---- CORREÇÃO AQUI
        } else if ("Monolito".equals(evento.getOrigem())) {
            logger.info("Evento AlunoAtualizado ignorado (origem: monolito).");
        } else {
            logger.warn("Mensagem recebida não é do tipo AlunoAtualizado.");
        }
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("rabbitmq.alunos.reverse.queue"),
            exchange = @Exchange(value = "alunos.reverse.exchange", type = "direct"),
            key = "aluno.excluido"
        )
    )
    public void receberAlunoExcluido(Message message, AlunoExcluidoNoMicrosservicoEvent evento) {
        String messageBody = new String(message.getBody());
        logger.info("Mensagem recebida (raw): {}", messageBody);
        if (!"Monolito".equals(evento.getOrigem()) && "AlunoExcluido".equals(evento.getEventType())) {
            logger.info("Evento AlunoExcluido recebido do microsserviço: {}", evento);
            alunoService.excluirAluno(evento.getId(), evento.getOrigem()); // Passe a origem aqui
        } else if ("Monolito".equals(evento.getOrigem())) {
            logger.info("Evento AlunoExcluido ignorado (origem: monolito).");
        } else {
            logger.warn("Mensagem recebida não é do tipo AlunoExcluido.");
        }
    }

    // Método auxiliar para converter AlunoCriadoNoMicrosservicoEvent para AlunoDTO
    private AlunoDTO converterParaDTO(AlunoCriadoNoMicrosservicoEvent evento) {
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
        dto.setDataNascimento(evento.getDataNascimento() != null ? LocalDate.parse(evento.getDataNascimento(), dateFormatter) : null);
        dto.setEventType(evento.getEventType());
        dto.setOrigem(evento.getOrigem());

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
        dto.setSenha(evento.getSenha());
        dto.setOrigem(evento.getOrigem());
        dto.setEventType(evento.getEventType());
        return dto;
    }
}