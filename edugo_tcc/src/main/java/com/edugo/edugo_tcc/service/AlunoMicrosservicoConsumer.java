package com.edugo.edugo_tcc.service;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.event.AlunoAtualizadoNoMicrosservicoEvent;
import com.edugo.edugo_tcc.event.AlunoCriadoNoMicrosservicoEvent;
import com.edugo.edugo_tcc.event.AlunoExcluidoNoMicrosservicoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AlunoMicrosservicoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlunoMicrosservicoConsumer.class);

    @Autowired
    private AlunoService alunoService; // Injete o seu AlunoService do monolito

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // Assumindo o formato AAAA-MM-DD

    @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    public void receberAlunoCriado(AlunoCriadoNoMicrosservicoEvent evento) {
        logger.info("Evento AlunoCriado recebido do microsserviço: {}", evento);
        AlunoDTO alunoDTO = converterParaDTO(evento);
        alunoService.criarAluno(alunoDTO); // Use o seu serviço para criar o aluno no monolito
    }

    @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    public void receberAlunoAtualizado(AlunoAtualizadoNoMicrosservicoEvent evento) {
        logger.info("Evento AlunoAtualizado recebido do microsserviço: {}", evento);
        AlunoDTO alunoDTO = converterParaDTO(evento);
        alunoService.atualizarAluno(evento.getId(), alunoDTO); // Use o seu serviço para atualizar o aluno no monolito
    }

    @RabbitListener(queues = "${rabbitmq.alunos.reverse.queue}")
    public void receberAlunoExcluido(AlunoExcluidoNoMicrosservicoEvent evento) {
        logger.info("Evento AlunoExcluido recebido do microsserviço: {}", evento);
        alunoService.excluirAluno(evento.getId()); // Use o seu serviço para excluir o aluno no monolito
    }

    // Método auxiliar para converter AlunoCriadoNoMicrosservicoEvent para AlunoDTO
    private AlunoDTO converterParaDTO(AlunoCriadoNoMicrosservicoEvent evento) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setCpf(evento.getCpf());
        // Converter String para LocalDate
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
        // A senha não deve ser propagada de volta do microsserviço (segurança)
        return dto;
    }

    // Método auxiliar para converter AlunoAtualizadoNoMicrosservicoEvent para AlunoDTO
    private AlunoDTO converterParaDTO(AlunoAtualizadoNoMicrosservicoEvent evento) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setCpf(evento.getCpf());
        // Converter String para LocalDate
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
        // A senha não deve ser propagada de volta do microsserviço (segurança)
        return dto;
    }
}