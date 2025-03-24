package com.edugo.edugo_tcc.event;

import java.util.UUID;

public class AlunoExcluidoEvent {

    private UUID id;

    public AlunoExcluidoEvent() {
    }

    public AlunoExcluidoEvent(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}