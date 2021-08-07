package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.TourniquetCard;
import uz.pdp.apphrmanagement.entity.TourniquetHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TourniquetHistoryRepository extends JpaRepository<TourniquetHistory, UUID> {
    List<TourniquetHistory> findAllByExitedAtBetween(Timestamp exitedAt, Timestamp exitedAt2);
    List<TourniquetHistory> findAllByEnteredAtBetween(Timestamp exitedAt, Timestamp exitedAt2);
}
