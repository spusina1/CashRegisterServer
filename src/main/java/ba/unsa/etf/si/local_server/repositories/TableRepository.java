package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "insert into tables(id, table_name) values (?1, ?2)"
    )
    int saveTable(Long id, String table_name);
}
