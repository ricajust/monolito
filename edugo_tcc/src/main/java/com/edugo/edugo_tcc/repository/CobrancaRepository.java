package com.edugo.edugo_tcc.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edugo.edugo_tcc.model.Cobranca;
import com.edugo.edugo_tcc.model.Pagamento;

@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, UUID> {
    List<Cobranca> findByPagamento(Pagamento pagamento);
}
