package com.rodrigo.first.api.controllers;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.services.CompanyService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.mockito.BDDMockito;
import org.mockito.Mockito;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class CompanyControllerTests {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private CompanyService companyService;

	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/companies/cnpj/";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "51463645000100";
	private static final String RAZAO_SOCIAL = "Empresa XYZ";

	@Test
	@WithMockUser
	public void testBuscarEmpresaCnpjInvalido() throws Exception {
		BDDMockito.given(this.companyService.findByCnpj(Mockito.anyString())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ));
	}

	@Test
	@WithMockUser
	public void testBuscarEmpresaCnpjValido() throws Exception {
		BDDMockito.given(this.companyService.findByCnpj(Mockito.anyString()))
				.willReturn(Optional.of(this.obterDadosEmpresa()));

		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.name", equalTo(RAZAO_SOCIAL)))
				.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	private Company obterDadosEmpresa() {
		Company empresa = new Company();
		empresa.setId(ID);
		empresa.setName(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		return empresa;
	}
}
