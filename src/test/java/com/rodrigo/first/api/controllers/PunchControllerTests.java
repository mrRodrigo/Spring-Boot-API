package com.rodrigo.first.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.first.api.dtos.PunchDto;
import com.rodrigo.first.api.entities.Punch;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.PunchEnum;
import com.rodrigo.first.api.enums.RoleEnum;
import com.rodrigo.first.api.services.PunchService;
import com.rodrigo.first.api.services.WorkerService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)

public class PunchControllerTests {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PunchService punchServive;
	
	@MockBean
	private WorkerService workerService;
	
	private static final String URL_BASE = "/api/punches/";
	private static final Long ID_FUNCIONARIO = 1L;
	private static final Long ID_LANCAMENTO = 1L;
	private static final String TIPO = PunchEnum.START_WORK.name();
	private static final Date DATA = new Date();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	@WithMockUser
	public void testCadastrarLancamento() throws Exception {
		Punch lancamento = obterDadosLancamento();
		BDDMockito.given(this.workerService.findById(Mockito.anyLong())).willReturn(Optional.of(new Worker()));
		BDDMockito.given(this.punchServive.persist(Mockito.any(Punch.class))).willReturn(lancamento);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO))
				.andExpect(jsonPath("$.data.type").value(TIPO))
				.andExpect(jsonPath("$.data.date").value(this.dateFormat.format(DATA)))
				.andExpect(jsonPath("$.data.workerId").value(ID_FUNCIONARIO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCadastrarLancamentoFuncionarioIdInvalido() throws Exception {
		BDDMockito.given(this.workerService.findById(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testRemoverLancamento() throws Exception {
		BDDMockito.given(this.punchServive.findById(Mockito.anyLong())).willReturn(Optional.of(new Punch()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testRemoverLancamentoAcessoNegado() throws Exception {
		BDDMockito.given(this.punchServive.findById(Mockito.anyLong())).willReturn(Optional.of(new Punch()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private String obterJsonRequisicaoPost() throws JsonProcessingException {
		PunchDto lancamentoDto = new PunchDto();
		lancamentoDto.setId(null);
		lancamentoDto.setDate(this.dateFormat.format(DATA));
		lancamentoDto.setType(TIPO);
		lancamentoDto.setWorkerId(ID_FUNCIONARIO);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(lancamentoDto);
	}

	private Punch obterDadosLancamento() {
		Punch lancamento = new Punch();
		lancamento.setId(ID_LANCAMENTO);
		lancamento.setData(DATA);
		lancamento.setType(PunchEnum.valueOf(TIPO));
		lancamento.setWorker(new Worker());
		lancamento.getWorker().setId(ID_FUNCIONARIO);
		return lancamento;
	}	
}
