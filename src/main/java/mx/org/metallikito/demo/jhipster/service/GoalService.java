package mx.org.metallikito.demo.jhipster.service;

import mx.org.metallikito.demo.jhipster.domain.Goal;
import mx.org.metallikito.demo.jhipster.repository.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Goal.
 */
@Service
@Transactional
public class GoalService {

    private final Logger log = LoggerFactory.getLogger(GoalService.class);
    
    @Inject
    private GoalRepository goalRepository;
    
    /**
     * Save a goal.
     * 
     * @param goal the entity to save
     * @return the persisted entity
     */
    public Goal save(Goal goal) {
        log.debug("Request to save Goal : {}", goal);
        Goal result = goalRepository.save(goal);
        return result;
    }

    /**
     *  Get all the goals.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Goal> findAll() {
        log.debug("Request to get all Goals");
        List<Goal> result = goalRepository.findAll();
        return result;
    }

    /**
     *  Get one goal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Goal findOne(Long id) {
        log.debug("Request to get Goal : {}", id);
        Goal goal = goalRepository.findOne(id);
        return goal;
    }

    /**
     *  Delete the  goal by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Goal : {}", id);
        goalRepository.delete(id);
    }
}
