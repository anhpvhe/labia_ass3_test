package lab.ia.ExpenseManagement.Repositories;


import lab.ia.ExpenseManagement.Models.Record;
import lab.ia.ExpenseManagement.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findRecordsByUser(User user, Pageable pageable);

    Record findRecordByUserAndId(User user, Long id);

    Page<Record> findRecordsByUserAndDateBetween(User user, Date date, Date date2, Pageable pageable);

    Page<Record> findRecordsByUserAndDateAfter(User user, Date date, Pageable pageable);

    Page<Record> findRecordsByUserAndDateBefore(User user, Date date, Pageable pageable);
}
