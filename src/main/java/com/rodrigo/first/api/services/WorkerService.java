package com.rodrigo.first.api.services;
import java.util.Optional;
import com.rodrigo.first.api.entities.Worker;

public interface WorkerService {
	/**
	 * Persiste um funcionário na base de dados.
	 * 
	 * @param Worker
	 * @return Worker
	 */
	Worker persist(Worker worker);
	
	/**
	 * Busca e retorna um funcionário dado um CPF.
	 * 
	 * @param cpf
	 * @return Optional<Worker>
	 */
	Optional<Worker> findByCpf(String cpf);
	
	/**
	 * Busca e retorna um funcionário dado um email.
	 * 
	 * @param email
	 * @return Optional<Worker>
	 */
	Optional<Worker> findByEmail(String email);
	
	/**
	 * Busca e retorna um funcionário por ID.
	 * 
	 * @param id
	 * @return Optional<Worker>
	 */
	Optional<Worker> findById(Long id);
}
