package com.rodrigo.first.api.dtos;


import java.util.Optional;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class WorkerDto {
	private Long id;
	private String name;
	private String email;
	private Optional<String> password = Optional.empty();
	private Optional<String> hourCost = Optional.empty();
	private Optional<String> qtdHoursDay = Optional.empty();
	private Optional<String> qtdHoursLunch = Optional.empty();

	public WorkerDto() {
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

	public Optional<String> getPassword() {
		return password;
	}

	public void setPassword(Optional<String> password) {
		this.password = password;
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

	@Override
	public String toString() {
		return "WorkerDto [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", hourCost="
				+ hourCost + ", qtdHoursDay=" + qtdHoursDay + ", qtdHoursLunch=" + qtdHoursLunch + "]";
	}

}
