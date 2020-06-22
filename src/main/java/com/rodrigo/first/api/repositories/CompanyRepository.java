package com.rodrigo.first.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.first.api.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	@Transactional(readOnly = true) //n precisa travar o banco
	Company findByCnpj(String cnpj);
}
