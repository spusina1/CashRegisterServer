package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {
    Optional<CashRegister> findDistinctFirstByTakenIsFalse();

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "insert into cash_registers(id, name, taken) values (?1, ?2, false, false) on conflict (id) do nothing"
    )
    int saveCashRegister(Long id, String name);
}
