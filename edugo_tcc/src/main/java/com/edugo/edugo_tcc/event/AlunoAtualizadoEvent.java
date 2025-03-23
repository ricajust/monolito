package com.edugo.edugo_tcc.event;

import com.edugo.edugo_tcc.dto.AlunoDTO;

public class AlunoAtualizadoEvent {

    private AlunoDTO aluno;

    public AlunoAtualizadoEvent() {
    }

    public AlunoAtualizadoEvent(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }
}