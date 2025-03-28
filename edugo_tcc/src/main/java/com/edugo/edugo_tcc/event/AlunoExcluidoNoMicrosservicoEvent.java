package com.edugo.edugo_tcc.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoExcluidoNoMicrosservicoEvent {
    public UUID id;
    public String origem;
    private String eventType;
}
