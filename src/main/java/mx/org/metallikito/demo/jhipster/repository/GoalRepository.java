package mx.org.metallikito.demo.jhipster.repository;

import mx.org.metallikito.demo.jhipster.domain.Goal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Goal entity.
 */
@SuppressWarnings("unused")
public interface GoalRepository extends JpaRepository<Goal,Long> {

    @Query("select goal from Goal goal where goal.user.login = ?#{principal.username}")
    List<Goal> findByUserIsCurrentUser();

}
