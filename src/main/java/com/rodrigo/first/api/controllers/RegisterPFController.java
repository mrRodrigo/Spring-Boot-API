package com.rodrigo.first.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.RoleEnum;
import com.rodrigo.first.api.response.Response;
import com.rodrigo.first.api.dtos.RegisterPFDto;
import com.rodrigo.first.api.entities.Company;
import com.rodrigo.first.api.services.CompanyService;
import com.rodrigo.first.api.services.WorkerService;
import com.rodrigo.first.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/register-pf")
@CrossOrigin(origins = "*")
public class RegisterPFController {
    private static final Logger log = LoggerFactory.getLogger(RegisterPFController.class);
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private WorkerService workerService;

	public RegisterPFController() {
	}

	/**
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<RegisterPFDto>> cadastrar(@Valid @RequestBody RegisterPFDto registerPFDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", registerPFDto.toString());
		Response<RegisterPFDto> response = new Response<RegisterPFDto>();

		validate(registerPFDto, result);
		Worker worker = this.convertWorker(registerPFDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Company> company = this.companyService.findByCnpj(registerPFDto.getCnpj());
		company.ifPresent(emp -> worker.setCompany(emp));
		this.workerService.persist(worker);

		response.setData(this.converterDto(worker));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funcionário não existe na base de dados.
	 * 
	 * @param registerPFDto
	 * @param result
	 */
	private void validate(RegisterPFDto registerPFDto, BindingResult result) {
		Optional<Company> company = this.companyService.findByCnpj(registerPFDto.getCnpj());
		if (!company.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.workerService.findByCpf(registerPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.workerService.findByEmail(registerPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param registerPFDto
	 * @param result
	 * @return Worker
	 * @throws NoSuchAlgorithmException
	 */
	private Worker convertWorker(RegisterPFDto registerPFDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Worker worker = new Worker();
		worker.setName(registerPFDto.getName());
		worker.setEmail(registerPFDto.getEmail());
		worker.setCpf(registerPFDto.getCpf());
		worker.setRole(RoleEnum.ROLE_USUARIO);
		worker.setPassword(PasswordUtils.generateBCrypt(registerPFDto.getPassword()));
		registerPFDto.getQtdHoursLunch()
				.ifPresent(getQtdHoursLunch -> worker.setQtdHoursLunch(Float.valueOf(getQtdHoursLunch)));
		registerPFDto.getQtdHoursDay()
				.ifPresent(getQtdHoursDay -> worker.setQtdHoursDay(Float.valueOf(getQtdHoursDay)));
		registerPFDto.getHourCost().ifPresent(getHourCost -> worker.setHourCost(new BigDecimal(getHourCost)));

		return worker;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return CadastroPFDto
	 */
	private RegisterPFDto converterDto(Worker worker) {
		RegisterPFDto registerPFDto = new RegisterPFDto();
		registerPFDto.setId(worker.getId());
		registerPFDto.setName(worker.getName());
		registerPFDto.setEmail(worker.getEmail());
		registerPFDto.setCpf(worker.getCpf());
		registerPFDto.setCnpj(worker.getCompany().getCnpj());
		
		worker.getQtdHoursLunchOpt().ifPresent(getQtdHoursLunch -> registerPFDto
				.setQtdHoursLunch(Optional.of(Float.toString(getQtdHoursLunch))));
		
		worker.getQtdHoursDayOpt().ifPresent(
				setQtdHoursDay -> registerPFDto.setQtdHoursDay(Optional.of(Float.toString(setQtdHoursDay))));
		
		worker.getHourCostOpt()
				.ifPresent(setHourCost -> registerPFDto.setHourCost(Optional.of(setHourCost.toString())));

		return registerPFDto;
	}
}
