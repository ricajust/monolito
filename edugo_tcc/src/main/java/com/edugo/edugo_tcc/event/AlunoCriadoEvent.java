package com.edugo.edugo_tcc.event;

import com.edugo.edugo_tcc.dto.AlunoDTO;

public class AlunoCriadoEvent {

    private AlunoDTO aluno;
    public AlunoCriadoEvent(){}

    public AlunoCriadoEvent(AlunoDTO aluno){
        this.aluno = aluno;
    }

    public AlunoDTO getAluno(){
        return aluno;
    }

    public void setAluno(AlunoDTO aluno){
        this.aluno = aluno;
    }
}
