package mx.org.metallikito.demo.jhipster.service;

import mx.org.metallikito.demo.jhipster.domain.Metric;
import mx.org.metallikito.demo.jhipster.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Metric.
 */
@Service
@Transactional
public class MetricService {

    private final Logger log = LoggerFactory.getLogger(MetricService.class);
    
    @Inject
    private MetricRepository metricRepository;
    
    /**
     * Save a metric.
     * 
     * @param metric the entity to save
     * @return the persisted entity
     */
    public Metric save(Metric metric) {
        log.debug("Request to save Metric : {}", metric);
        Metric result = metricRepository.save(metric);
        return result;
    }

    /**
     *  Get all the metrics.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Metric> findAll(Pageable pageable) {
        log.debug("Request to get all Metrics");
        Page<Metric> result = metricRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one metric by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Metric findOne(Long id) {
        log.debug("Request to get Metric : {}", id);
        Metric metric = metricRepository.findOne(id);
        return metric;
    }

    /**
     *  Delete the  metric by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Metric : {}", id);
        metricRepository.delete(id);
    }
}
