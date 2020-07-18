package com.rodrigo.first.api.controllers;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.first.api.dtos.CompanyDto;
import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.response.Response;
import com.rodrigo.first.api.services.CompanyService;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
	private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	private CompanyService companyService;

	public CompanyController() {
	}

	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<CompanyDto>>
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<CompanyDto>> findByCnpj(@PathVariable("cnpj") String cnpj) {
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		Response<CompanyDto> response = new Response<CompanyDto>();
		Optional<Company> company = companyService.findByCnpj(cnpj);

		if (!company.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.convertCompanyDto(company.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Popula um DTO com os dados de uma empresa.
	 * 
	 * @param company
	 * @return CompanyDto
	 */
	private CompanyDto convertCompanyDto(Company company) {
		CompanyDto companydto = new CompanyDto();
		companydto.setId(company.getId());
		companydto.setCnpj(company.getCnpj());
		companydto.setName(company.getName());

		return companydto;
	}
}
