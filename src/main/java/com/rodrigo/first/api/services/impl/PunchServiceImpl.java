package com.rodrigo.first.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rodrigo.first.api.entities.Punch;
import com.rodrigo.first.api.repositories.PunchRepository;
import com.rodrigo.first.api.services.PunchService;

@Service
public class PunchServiceImpl implements PunchService {

	private static final Logger log = LoggerFactory.getLogger(PunchServiceImpl.class);

	@Autowired
	private PunchRepository workerRepository;

	public Page<Punch> findByWorkerById(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando lançamentos para o funcionário ID {}", funcionarioId);
		return this.workerRepository.findByWorkerId(funcionarioId, pageRequest);
	}
	
	@Cacheable("lancamentoPorId")
	public Optional<Punch> findById(Long id) {
		log.info("Buscando um lançamento pelo ID {}", id);
		return this.workerRepository.findById(id);
	}
	
	@CachePut("lancamentoPorId")
	public Punch persist(Punch lancamento) {
		log.info("Persistindo o lançamento: {}", lancamento);
		return this.workerRepository.save(lancamento);
	}
	
	public void remove(Long id) {
		log.info("Removendo o lançamento ID {}", id);
		this.workerRepository.deleteById(id);
	}
}
