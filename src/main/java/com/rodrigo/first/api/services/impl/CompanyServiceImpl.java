package com.rodrigo.first.api.services.impl;



import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.repositories.CompanyRepository;
import com.rodrigo.first.api.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Optional<Company> findByCnpj(String cnpj) {
		log.info("Buscando uma empresa para o CNPJ {}", cnpj);
		return Optional.ofNullable(companyRepository.findByCnpj(cnpj));
	}

	@Override
	public Company persist(Company company) {
		log.info("Persistindo empresa: {}", company);
		return this.companyRepository.save(company);
	}

}
