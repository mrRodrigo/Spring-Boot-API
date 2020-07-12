package com.rodrigo.first.api.services;
import java.util.Optional;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.repositories.WorkerRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class WorkerServiceTest {
	@MockBean
	private WorkerRepository workerRepository;

	@Autowired
	private WorkerService workerService;

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito.given(this.workerRepository.save(Mockito.any(Worker.class))).willReturn(new Worker());
		BDDMockito.given(this.workerRepository.findByEmail(Mockito.anyString())).willReturn(new Worker());
		BDDMockito.given(this.workerRepository.findByCpf(Mockito.anyString())).willReturn(new Worker());
		BDDMockito.given(this.workerRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Worker()));
	}

	@Test
	public void testPersistirFuncionario() {
		Worker funcionario = this.workerService.persist(new Worker());

		assertNotNull(funcionario);
	}

	@Test
	public void testBuscarFuncionarioPorId() {
		Optional<Worker> worker = this.workerService.findById(1L);

		assertTrue(worker.isPresent());
	}

	@Test
	public void testBuscarFuncionarioPorEmail() {
		Optional<Worker> worker = this.workerService.findByEmail("email@email.com");

		assertTrue(worker.isPresent());
	}

	@Test
	public void testBuscarFuncionarioPorCpf() {
		Optional<Worker> worker = this.workerService.findByCpf("24291173474");

		assertTrue(worker.isPresent());
	}
}
