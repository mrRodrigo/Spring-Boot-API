package com.rodrigo.first.api.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.repositories.CompanyRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class CompanyServiceTest {
	@MockBean
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyService companyService;

	private static final String CNPJ = "51463645000100";

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito.given(this.companyRepository.findByCnpj(Mockito.anyString())).willReturn(new Company());
		BDDMockito.given(this.companyRepository.save(Mockito.any(Company.class))).willReturn(new Company());
	}

	@Test
	public void testBuscarEmpresaPorCnpj() {
		Optional<Company> company = this.companyService.findByCnpj(CNPJ);

		assertTrue(company.isPresent());
	}
	
	@Test
	public void testPersistirEmpresa() {
		Company company = this.companyService.persist(new Company());

		assertNotNull(company);
	}
}
