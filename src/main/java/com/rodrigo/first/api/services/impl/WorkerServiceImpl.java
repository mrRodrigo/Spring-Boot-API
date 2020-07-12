package com.rodrigo.first.api.services.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.repositories.WorkerRepository;
import com.rodrigo.first.api.services.WorkerService;

@Service
public class WorkerServiceImpl implements WorkerService {
	private static final Logger log = LoggerFactory.getLogger(WorkerServiceImpl.class);

	@Autowired
	private WorkerRepository workerRepository;
	
	public Worker persist(Worker worker) {
		log.info("Persistindo funcionário: {}", worker);
		return this.workerRepository.save(worker);
	}
	
	public Optional<Worker> findByCpf(String cpf) {
		log.info("Buscando funcionário pelo CPF {}", cpf);
		return Optional.ofNullable(this.workerRepository.findByCpf(cpf));
	}
	
	public Optional<Worker> findByEmail(String email) {
		log.info("Buscando funcionário pelo email {}", email);
		return Optional.ofNullable(this.workerRepository.findByEmail(email));
	}
	
	public Optional<Worker> findById(Long id) {
		log.info("Buscando funcionário pelo IDl {}", id);
		return this.workerRepository.findById(id);
	}
}
