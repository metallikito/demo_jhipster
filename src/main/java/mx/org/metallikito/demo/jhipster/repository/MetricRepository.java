package mx.org.metallikito.demo.jhipster.repository;

import mx.org.metallikito.demo.jhipster.domain.Metric;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Metric entity.
 */
@SuppressWarnings("unused")
public interface MetricRepository extends JpaRepository<Metric,Long> {

}
