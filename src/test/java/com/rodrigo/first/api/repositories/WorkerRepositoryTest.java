package com.rodrigo.first.api.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.RoleEnum;
import com.rodrigo.first.api.utils.PasswordUtils;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class WorkerRepositoryTest {
	
	@Autowired
	private WorkerRepository workerRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	private static final String EMAIL = "email@email.com";
	private static final String CPF = "24291173474";
	
	@BeforeAll
	public void setUp() throws Exception {
		Company empresa = this.companyRepository.save(getCompany());
		this.workerRepository.save(getWorker(empresa));
	}
	
	@AfterAll
	public final void tearDown() {
		this.companyRepository.deleteAll();
	}
	
	@Test
	public void testFindWorkerByEmail() {
		Worker func = this.workerRepository.findByEmail(EMAIL);

		assertEquals(EMAIL, func.getEmail());
	}
	
	@Test
	public void testFindWorkerByCpf() {
		Worker func = this.workerRepository.findByCpf(CPF);

		assertEquals(CPF, func.getCpf());
	}
	
	@Test
	public void testFindWorkerByEmailAndCpf() {
		Worker func = this.workerRepository.findByCpfOrEmail(CPF, EMAIL);

		assertNotNull(func);
	}
	
	@Test
	public void testFindWorkerInvalidEMAIL() {
		Worker func = this.workerRepository.findByCpfOrEmail(CPF, "email@invalido.com");

		assertNotNull(func);
	}
	
	@Test
	public void testFindWorkerInvalidCPF() {
		Worker func = this.workerRepository.findByCpfOrEmail("kasdkasdasdk", EMAIL);

		assertNotNull(func);
	}
	
	private Worker getWorker(Company empresa) throws NoSuchAlgorithmException {
		Worker funcionario = new Worker();
		funcionario.setName("Fulano de Tal");
		funcionario.setRole(RoleEnum.ROLE_USUARIO);
		funcionario.setPassword(PasswordUtils.generateBCrypt("123456"));
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setCompany(empresa);
		return funcionario;
	}

	private Company getCompany() {
		Company empresa = new Company();
		empresa.setName("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
}
