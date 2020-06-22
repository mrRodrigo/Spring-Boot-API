package com.rodrigo.first.api.entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.rodrigo.first.api.enums.RoleEnum;

@Entity
@Table(name = "worker")
public class Worker {
	private static final long serialVersionUID = -5754246207015712518L;
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private BigDecimal hourCost;
	private Float qtdHoursDay;
	private Float qtdHoursLunch;
	private RoleEnum role;
	private Date createDate;
	private Date updateDate;
	private Company company;
	private List<Punch> punches;

	public Worker() {
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() { return name; }

	public void setName(String nome) { this.name = nome; }

	@Column(name = "email", nullable = false)
	public String getEmail() { return email; }

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(name = "hour_cost", nullable = true)
	public BigDecimal getHourCost() {
		return hourCost;
	}
	
	@Transient
	public Optional<BigDecimal> getHourCostOpt() {
		return Optional.ofNullable(hourCost);
	}

	public void setHourCost(BigDecimal hourCost) {
		this.hourCost = hourCost;
	}

	@Column(name = "qtd_hours_day", nullable = true)
	public Float getQtdHoursDay() {
		return qtdHoursDay;
	}
	
	@Transient
	public Optional<Float> getQtdHoursDayOpt() {
		return Optional.ofNullable(qtdHoursDay);
	}

	public void setQtdHoursDay(Float qtdHoursDay) {
		this.qtdHoursDay = qtdHoursDay;
	}

	@Column(name = "qtd_hours_lunch", nullable = true)
	public Float getQtdHoursLunch() {
		return qtdHoursLunch;
	}
	
	@Transient
	public Optional<Float> getQtdHoursLunchOpt() {
		return Optional.ofNullable(qtdHoursLunch);
	}

	public void setQtdHoursLunch(Float qtdHoursLunch) {
		this.qtdHoursLunch = qtdHoursLunch;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_date", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Punch> getPunches() {
		return punches;
	}

	public void setPunches(List<Punch> punches) {
		this.punches = punches;
	}
	
	@PreUpdate
    public void preUpdate() {
		updateDate = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        createDate = atual;
        updateDate = atual;
    }

	@Override
	public String toString() {
		return "Funcionario [id=" + id + ", nome=" + name + ", email=" + email + ", senha=" + password + ", cpf=" + cpf
				+ ", valorHora=" + qtdHoursDay  + "]";
	}
}
