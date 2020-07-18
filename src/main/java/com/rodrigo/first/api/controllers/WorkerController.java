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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.first.api.dtos.WorkerDto;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.response.Response;
import com.rodrigo.first.api.services.WorkerService;
import com.rodrigo.first.api.utils.PasswordUtils;



@RestController
@RequestMapping("/api/workers")
@CrossOrigin(origins = "*")
public class WorkerController {
	private static final Logger log = LoggerFactory.getLogger(WorkerController.class);

	@Autowired
	private WorkerService workerService;

	public WorkerController() {
	}

	/**
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param funcionarioDto
	 * @param result
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<WorkerDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody WorkerDto workerDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", workerDto.toString());
		Response<WorkerDto> response = new Response<WorkerDto>();

		Optional<Worker> worker = this.workerService.findById(id);
		if (!worker.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
		}

		this.updateWorker(worker.get(), workerDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando funcionário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.workerService.persist(worker.get());
		response.setData(this.converterWorkerDto(worker.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void updateWorker(Worker worker, WorkerDto workerDto, BindingResult result) throws NoSuchAlgorithmException {
		worker.setName(workerDto.getName());

		if (!worker.getEmail().equals(workerDto.getEmail())) {
			this.workerService.findByEmail(workerDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			worker.setEmail(workerDto.getEmail());
		}

		worker.setQtdHoursLunch(null);
		workerDto.getQtdHoursLunch()
				.ifPresent(lunch -> worker.setQtdHoursLunch(Float.valueOf(lunch)));

		worker.setQtdHoursDay(null);
		workerDto.getQtdHoursDay()
				.ifPresent(hrs -> worker.setQtdHoursDay(Float.valueOf(hrs)));

		worker.setHourCost(null);
		workerDto.getHourCost().ifPresent(valorHora -> worker.setHourCost(new BigDecimal(valorHora)));

		if (workerDto.getPassword().isPresent()) {
			worker.setPassword(PasswordUtils.generateBCrypt(workerDto.getPassword().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private WorkerDto converterWorkerDto(Worker worker) {
		WorkerDto workerDto = new WorkerDto();
		workerDto.setId(worker.getId());
		workerDto.setEmail(worker.getEmail());
		workerDto.setName(worker.getName());
		worker.getQtdHoursLunchOpt().ifPresent( 
				lunch -> workerDto.setQtdHoursLunch(Optional.of(Float.toString(lunch))));
		worker.getQtdHoursDayOpt().ifPresent(
				day -> workerDto.setQtdHoursDay(Optional.of(Float.toString(day))));
		worker.getHourCostOpt()
				.ifPresent(cost -> workerDto.setHourCost(Optional.of(cost.toString())));

		return workerDto;
	}

}
