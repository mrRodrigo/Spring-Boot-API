package com.rodrigo.first.api.dtos;
import java.math.BigDecimal;
import java.util.Optional;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class RegisterPFDto {
	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private Optional<String> hourCost = Optional.empty();
	private Optional<String> qtdHoursDay = Optional.empty();
	private Optional<String> qtdHoursLunch = Optional.empty();
	private String cnpj;

	public RegisterPFDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
	@Email(message="Email inválido.")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotEmpty(message = "Senha não pode ser vazia.")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotEmpty(message = "CPF não pode ser vazio.")
	@CPF(message="CPF inválido")
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Optional<String> getHourCost() {
		return hourCost;
	}

	public void setHourCost(Optional<String> hourCost) {
		this.hourCost = hourCost;
	}

	public Optional<String> getQtdHoursDay() {
		return qtdHoursDay;
	}

	public void setQtdHoursDay(Optional<String> qtdHoursDay) {
		this.qtdHoursDay = qtdHoursDay;
	}

	public Optional<String> getQtdHoursLunch() {
		return qtdHoursLunch;
	}

	public void setQtdHoursLunch(Optional<String> qtdHoursLunch) {
		this.qtdHoursLunch = qtdHoursLunch;
	}

	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message="CNPJ inválido.")
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "FuncionarioDto [id=" + id + ", nome=" + name + ", email=" + email + ", senha=" + password + ", cpf=" + cpf
				+ ", valorHora=" + hourCost + ", qtdHorasTrabalhoDia=" + qtdHoursDay + ", qtdHorasAlmoco="
				+ qtdHoursLunch + ", cnpj=" + cnpj + "]";
	}
}
