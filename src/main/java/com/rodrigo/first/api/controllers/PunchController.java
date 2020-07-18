package com.rodrigo.first.api.controllers;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.text.ParseException;
import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.first.api.services.PunchService;
import com.rodrigo.first.api.services.WorkerService;
import com.rodrigo.first.api.entities.Punch;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.PunchEnum;
import com.rodrigo.first.api.response.Response;
import com.rodrigo.first.api.dtos.PunchDto;

@RestController
@RequestMapping("/api/punches")
@CrossOrigin(origins = "*")
public class PunchController {
	private static final Logger log = LoggerFactory.getLogger(PunchController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private PunchService punchService;

	@Autowired
	private WorkerService workerService;
	
	@Value("${paginate.qtd}")
	private int qtdPorPagina;

	public PunchController() {
	}

	/**
	 * Retorna a listagem de lançamentos de um funcionário.
	 * 
	 * @param funcionarioId
	 * @return ResponseEntity<Response<PunchDto>>
	 */
	@GetMapping(value = "/worker/{workerId}")
	public ResponseEntity<Response<Page<PunchDto>>> listByWorkerId(
			@PathVariable("workerId") Long workerId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", workerId, pag);
		Response<Page<PunchDto>> response = new Response<Page<PunchDto>>();

		Page<Punch> lancamentos = this.punchService.findByWorkerById(workerId, PageRequest.of(0, 10));
		Page<PunchDto> lancamentosDto = lancamentos.map(punch -> this.convertPunchDto(punch));

		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PunchDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PunchDto>> ListById(@PathVariable("id") Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		Response<PunchDto> response = new Response<PunchDto>();
		Optional<Punch> punch = this.punchService.findById(id);

		if (!punch.isPresent()) {
			log.info("Lançamento não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.convertPunchDto(punch.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Adiciona um novo lançamento.
	 * 
	 * @param punch
	 * @param result
	 * @return ResponseEntity<Response<PunchDto>>
	 * @throws ParseException 
	 */
	@PostMapping
	public ResponseEntity<Response<PunchDto>> add(@Valid @RequestBody PunchDto punchDto,
			BindingResult result) throws ParseException {
		log.info("Adicionando lançamento: {}", punchDto.toString());
		Response<PunchDto> response = new Response<PunchDto>();
		validateWorker(punchDto, result);
		Punch punch = this.convertDtoToPunch(punchDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		punch = this.punchService.persist(punch);
		response.setData(this.convertPunchDto(punch));
		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados de um lançamento.
	 * 
	 * @param id
	 * @param punchDto
	 * @return ResponseEntity<Response<Punch>>
	 * @throws ParseException 
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PunchDto>> update(@PathVariable("id") Long id,
			@Valid @RequestBody PunchDto punchDto, BindingResult result) throws ParseException {
		log.info("Atualizando lançamento: {}", punchDto.toString());
		Response<PunchDto> response = new Response<PunchDto>();
		validateWorker(punchDto, result);
		punchDto.setId(Optional.of(id));
		Punch punch = this.convertDtoToPunch(punchDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		punch = this.punchService.persist(punch);
		response.setData(this.convertPunchDto(punch));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Punch>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remove(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Punch> punch = this.punchService.findById(id);

		if (!punch.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.punchService.remove(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Valida um funcionário, verificando se ele é existente e válido no
	 * sistema.
	 * 
	 * @param punchDto
	 * @param result
	 */
	private void validateWorker(PunchDto punchDto, BindingResult result) {
		if (punchDto.getWorkerId() == null) {
			result.addError(new ObjectError("worker", "Funcionário não informado."));
			return;
		}

		log.info("Validando funcionário id {}: ", punchDto.getWorkerId());
		Optional<Worker> worker = this.workerService.findById(punchDto.getWorkerId());
		if (!worker.isPresent()) {
			result.addError(new ObjectError("worker", "Funcionário não encontrado. ID inexistente."));
		}
	}

	/**
	 * Converte uma entidade lançamento para seu respectivo DTO.
	 * 
	 * @param punch
	 * @return PunchDto
	 */
	private PunchDto convertPunchDto(Punch punch) {
		PunchDto punchDto = new PunchDto();
		punchDto.setId(Optional.of(punch.getId()));
		punchDto.setDate(this.dateFormat.format(punch.getData()));
		punchDto.setType(punch.getType().toString());
		punchDto.setDescription(punch.getDescription());
		punchDto.setLocation(punch.getLocale());
		punchDto.setWorkerId(punch.getWorker().getId());

		return punchDto;
	}

	/**
	 * Converte um PunchDto para uma entidade Punch.
	 * 
	 * @param punchDto
	 * @param result
	 * @return Punch
	 * @throws ParseException 
	 */
	private Punch convertDtoToPunch(PunchDto punchDto, BindingResult result) throws ParseException {
		Punch punch = new Punch();

		if (punchDto.getId().isPresent()) {
			Optional<Punch> lanc = this.punchService.findById(punchDto.getId().get());
			if (lanc.isPresent()) {
				punch = lanc.get();
			} else {
				result.addError(new ObjectError("punch", "Lançamento não encontrado."));
			}
		} else {
			punch.setWorker(new Worker());
			punch.getWorker().setId(punchDto.getWorkerId());
		}

		punch.setDescription(punchDto.getDescription());
		punch.setLocale(punchDto.getLocation());
		punch.setData(this.dateFormat.parse(punchDto.getDate()));

		if (EnumUtils.isValidEnum(PunchEnum.class, punchDto.getType())) {
			punch.setType(PunchEnum.valueOf(punchDto.getType()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return punch;
	}

}
