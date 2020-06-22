package com.rodrigo.first.api.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;


import com.rodrigo.first.api.entities.Company;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class CompanyRepositoryTest {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	private static final String CNPJ = "51463645000100";

	@BeforeAll
	public void setUp() throws Exception {
		Company comp = new Company();
		comp.setName("Empresa de exemplo");
		comp.setCnpj(CNPJ);
		this.companyRepository.save(comp);
	}
	
	@AfterAll
    public final void tearDown() { 
		this.companyRepository.deleteAll();
	}
	
	@Test
	public void testBuscarPorCnpj() {
		Company emp = this.companyRepository.findByCnpj(CNPJ);
		
		assertEquals(CNPJ, emp.getCnpj());
	}
}
