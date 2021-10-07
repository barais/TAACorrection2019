package fr.istic.taa.repository;

import fr.istic.taa.domain.TODOList;
import fr.istic.taa.domain.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TODOList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TODOListRepository extends JpaRepository<TODOList, Long> {
    @Query("select tODOList from TODOList tODOList where tODOList.user.login = ?#{principal.username}")
    List<TODOList> findByUserIsCurrentUser();

    @Query("select task from Task task where task.todolist.id = ?1 and task.dateButoire between ?2 and ?3")
    List<Task> findTaskForTodoListBetween2Date(long todolist, LocalDate debut, LocalDate fin);
}
