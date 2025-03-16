package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Funcionario extends Usuario {
    //propriedades
    @Past
    private LocalDate dataContratacao;
    
    @NotBlank
    private int nivelAcesso;

    //metodos
    public List<Pagamento> calcularCobranca(Aluno aluno) {
        return new ArrayList<Pagamento>();
    }
}
