package com.rodrigo.first.api.dtos;
import java.util.Optional;

import org.hibernate.validator.constraints.NotEmpty;

public class PunchDto {
	private Optional<Long> id = Optional.empty();
	private String date;
	private String type;
	private String description;
	private String location;
	private Long workerId;
	
	public PunchDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}
	
}
