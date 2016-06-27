package mx.org.metallikito.demo.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.org.metallikito.demo.jhipster.domain.Metric;
import mx.org.metallikito.demo.jhipster.service.MetricService;
import mx.org.metallikito.demo.jhipster.web.rest.util.HeaderUtil;
import mx.org.metallikito.demo.jhipster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Metric.
 */
@RestController
@RequestMapping("/api")
public class MetricResource {

    private final Logger log = LoggerFactory.getLogger(MetricResource.class);
        
    @Inject
    private MetricService metricService;
    
    /**
     * POST  /metrics : Create a new metric.
     *
     * @param metric the metric to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metric, or with status 400 (Bad Request) if the metric has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/metrics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metric> createMetric(@Valid @RequestBody Metric metric) throws URISyntaxException {
        log.debug("REST request to save Metric : {}", metric);
        if (metric.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("metric", "idexists", "A new metric cannot already have an ID")).body(null);
        }
        Metric result = metricService.save(metric);
        return ResponseEntity.created(new URI("/api/metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("metric", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metrics : Updates an existing metric.
     *
     * @param metric the metric to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metric,
     * or with status 400 (Bad Request) if the metric is not valid,
     * or with status 500 (Internal Server Error) if the metric couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/metrics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metric> updateMetric(@Valid @RequestBody Metric metric) throws URISyntaxException {
        log.debug("REST request to update Metric : {}", metric);
        if (metric.getId() == null) {
            return createMetric(metric);
        }
        Metric result = metricService.save(metric);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("metric", metric.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metrics : get all the metrics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of metrics in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/metrics",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Metric>> getAllMetrics(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Metrics");
        Page<Metric> page = metricService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metrics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metrics/:id : get the "id" metric.
     *
     * @param id the id of the metric to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metric, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/metrics/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metric> getMetric(@PathVariable Long id) {
        log.debug("REST request to get Metric : {}", id);
        Metric metric = metricService.findOne(id);
        return Optional.ofNullable(metric)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /metrics/:id : delete the "id" metric.
     *
     * @param id the id of the metric to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/metrics/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        log.debug("REST request to delete Metric : {}", id);
        metricService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("metric", id.toString())).build();
    }

}
