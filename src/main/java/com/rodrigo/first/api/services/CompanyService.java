package com.rodrigo.first.api.services;

import java.util.Optional;

import com.rodrigo.first.api.entities.Company;


public interface CompanyService {
	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Company>
	 */
	Optional<Company> findByCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa na base de dados.
	 * 
	 * @param Company
	 * @return Company
	 */
	Company persist(Company company);
}
