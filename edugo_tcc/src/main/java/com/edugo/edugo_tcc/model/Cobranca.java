package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cobranca {
    //propriedades
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataPagamento;
    
    @Column(name = "metodo_pagamento")
    private String metodoPagamento;

    //métodos
    public void confirmarPagamento() {
    
    }
    public String gerarBoleto() {
        return "boleto";
    }
}
