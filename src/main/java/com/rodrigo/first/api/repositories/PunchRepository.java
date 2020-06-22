package com.rodrigo.first.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.first.api.entities.Punch;

@Transactional(readOnly = true)

@NamedQueries({
	@NamedQuery(
		name = "PunchRepository.findByWorkerId", 
		query = "SELECT lanc FROM punch lanc WHERE lanc.worker.id = :workerId"
	) 
})

public interface PunchRepository extends JpaRepository<Punch, Long> {
	
	List<Punch> findByWorkerId(@Param("workerId") Long workerId);

	Page<Punch> findByWorkerId(@Param("workerId") Long workerId, Pageable pageable);
}
