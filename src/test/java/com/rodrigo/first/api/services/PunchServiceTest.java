package com.rodrigo.first.api.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rodrigo.first.api.entities.Punch;
import com.rodrigo.first.api.repositories.PunchRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class PunchServiceTest {
	@MockBean
	private PunchRepository punchRepository;

	@Autowired
	private PunchService punchService;

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito
				.given(this.punchRepository.findByWorkerId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<Punch>(new ArrayList<Punch>()));
		BDDMockito.given(this.punchRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Punch()));
		BDDMockito.given(this.punchRepository.save(Mockito.any(Punch.class))).willReturn(new Punch());
	}

	@Test
	public void testBuscarLancamentoPorFuncionarioId() {
		Page<Punch> punch = this.punchService.findByWorkerById(1L, PageRequest.of(0, 10));

		assertNotNull(punch);
	}

	@Test
	public void testBuscarLancamentoPorId() {
		Optional<Punch> punch = this.punchService.findById(1L);

		assertTrue(punch.isPresent());
	}

	@Test
	public void testPersistirLancamento() {
		Punch punch = this.punchService.persist(new Punch());

		assertNotNull(punch);
	}
}
