package com.rodrigo.first.api.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "company")
public class Company {
	private static final long serialVersionUID = 3960436649365666213L;
	
	private Long id;
	private String name;
	private String cnpj;
	private Date createDate;
	private Date updateDate;
	private List<Worker> workers;
	
	public Company() {
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
	public String getRazaoSocial() {
		return name;
	}

	public void setRazaoSocial(String name) {
		this.name = name;
	}

	@Column(name = "cnpj", nullable = false)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Column(name = "create_date", nullable = false)
	public Date getDataCriacao() {
		return createDate;
	}

	public void setDataCriacao(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_date", nullable = false)
	public Date getDataAtualizacao() {
		return updateDate;
	}

	public void setDataAtualizacao(Date updateDate) {
		this.updateDate = updateDate;
	}

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Worker> getFuncionarios() {
		return workers;
	}

	public void setFuncionarios(List<Worker> workers) {
		this.workers = workers;
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
		return "Company [id=" + id + ", razao Social=" + name + ", cnpj=" + cnpj + ", data Criacao=" + createDate
				+ ", data Atualizacao=" + updateDate + "]";
	}
}
