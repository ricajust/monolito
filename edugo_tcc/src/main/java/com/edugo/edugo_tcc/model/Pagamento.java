package com.edugo.edugo_tcc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {
    //propriedades
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull
    @Column(name = "valorTotal", nullable = false)
    private BigDecimal valorTotal;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    //metodos
    public String gerarBoletos() {
        return "boletos";
    }

    public void enviarEmailCobranca() {
        
    }
}
