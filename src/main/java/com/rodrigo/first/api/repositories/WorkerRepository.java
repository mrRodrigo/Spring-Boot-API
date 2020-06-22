package com.rodrigo.first.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.first.api.entities.Worker;

@Transactional(readOnly = true)//n precisa travar o banco
public interface WorkerRepository  extends JpaRepository<Worker, Long> {
	
	Worker findByCpf(String cpf);
	
	Worker findByEmail(String email);
	
	Worker findByCpfOrEmail(String cpf, String email);

}
