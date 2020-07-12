package com.rodrigo.first.api.controllers;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.first.api.dtos.RegisterPJDto;
import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.RoleEnum;
import com.rodrigo.first.api.response.Response;
import com.rodrigo.first.api.services.CompanyService;
import com.rodrigo.first.api.services.WorkerService;
import com.rodrigo.first.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/register-pj")
@CrossOrigin(origins = "*")
public class RegisterPJController {
	private static final Logger log = LoggerFactory.getLogger(RegisterPJController.class);

	@Autowired
	private WorkerService workerService;

	@Autowired
	private CompanyService companyService;

	public RegisterPJController() {
	}

	/**
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param registerPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<RegisterPJDto>> cadastrar(@Valid @RequestBody RegisterPJDto registerPJDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PJ: {}", registerPJDto.toString());
		Response<RegisterPJDto> response = new Response<RegisterPJDto>();

		validate(registerPJDto, result);
		Company company = this.convertDtoCompany(registerPJDto);
		Worker worker = this.convertDtoWorker(registerPJDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.companyService.persist(company);
		worker.setCompany(company);
		this.workerService.persist(worker);

		response.setData(this.convertRegisterPJDto(worker));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param registerPJDto
	 * @param result
	 */
	private void validate(RegisterPJDto registerPJDto, BindingResult result) {
		this.companyService.findByCnpj(registerPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

		this.workerService.findByCpf(registerPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.workerService.findByEmail(registerPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para empresa.
	 * 
	 * @param cadastroPJDto
	 * @return Company
	 */
	private Company convertDtoCompany(RegisterPJDto registerPJDto) {
		Company company = new Company();
		company.setCnpj(registerPJDto.getCnpj());
		company.setName(registerPJDto.getCompanyName());

		return company;
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param registerPJDto
	 * @param result
	 * @return Worker
	 * @throws NoSuchAlgorithmException
	 */
	private Worker convertDtoWorker(RegisterPJDto registerPJDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Worker worker = new Worker();
		worker.setName(registerPJDto.getName());
		worker.setEmail(registerPJDto.getEmail());
		worker.setCpf(registerPJDto.getCpf());
		worker.setRole(RoleEnum.ROLE_ADMIN);
		worker.setPassword(PasswordUtils.generateBCrypt(registerPJDto.getPassword()));

		return worker;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param Worker funcionario
	 * @return RegisterPJDto
	 */
	private RegisterPJDto convertRegisterPJDto(Worker worker) {
		RegisterPJDto registerPJDto = new RegisterPJDto();
		registerPJDto.setId(worker.getId());
		registerPJDto.setName(worker.getName());
		registerPJDto.setEmail(worker.getEmail());
		registerPJDto.setCpf(worker.getCpf());
		registerPJDto.setCompanyName(worker.getCompany().getName());
		registerPJDto.setCnpj(worker.getCompany().getCnpj());

		return registerPJDto;
	}
	
}
