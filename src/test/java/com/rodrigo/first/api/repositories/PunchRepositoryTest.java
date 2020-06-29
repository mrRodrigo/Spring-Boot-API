package com.rodrigo.first.api.repositories;
import java.util.Date;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.entities.Punch;
import com.rodrigo.first.api.enums.RoleEnum;
import com.rodrigo.first.api.enums.PunchEnum;
import com.rodrigo.first.api.utils.PasswordUtils;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class PunchRepositoryTest {
	
	@Autowired
	private PunchRepository punchRepository;
	
	@Autowired
	private WorkerRepository workerRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	 
	private Long workerId;
	
	@BeforeAll
	public void setUp() throws Exception {
		Company empresa = this.companyRepository.save(getCompany());
		
		Worker funcionario = this.workerRepository.save(getWorker(empresa));
		this.workerId = funcionario.getId();
		
		this.punchRepository.save(getPunch(funcionario));
		this.punchRepository.save(getPunch(funcionario));
	}
	
	@Test
	public void testFindPunchByID() {
		List<Punch> lancs = this.punchRepository.findByWorkerId(workerId);
		
		assertEquals(2, lancs.size());
	}
	
	@Test
	public void testFindPunchByIDPaginated() {
		PageRequest page = PageRequest.of(0, 10);
		Page<Punch> lancamentos = this.punchRepository.findByWorkerId(workerId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	
	private Punch getPunch(Worker funcionario) {
		Punch lancameto = new Punch();
		lancameto.setData(new Date());
		lancameto.setType(PunchEnum.START_LUNCH);
		lancameto.setWorker(funcionario);
		return lancameto;
	}

	private Worker getWorker(Company empresa) throws NoSuchAlgorithmException {
		Worker funcionario = new Worker();
		funcionario.setName("Fulano de Tal");
		funcionario.setRole(RoleEnum.ROLE_USUARIO);
		funcionario.setPassword(PasswordUtils.generateBCrypt("123456"));
		funcionario.setCpf("24291173474");
		funcionario.setEmail("email@email.com");
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
