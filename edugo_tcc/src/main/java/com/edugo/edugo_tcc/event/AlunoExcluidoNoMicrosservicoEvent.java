package com.edugo.edugo_tcc.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoExcluidoNoMicrosservicoEvent {
    @JsonProperty("Id")
    public UUID id;
    @JsonProperty("Origem")
    private String origem;
    @JsonProperty("EventType")
    private String eventType;
}
