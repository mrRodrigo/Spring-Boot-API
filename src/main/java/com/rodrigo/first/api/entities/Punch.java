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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rodrigo.first.api.enums.PunchEnum;


@Entity
@Table(name = "punch")
public class Punch {
	
	private static final long serialVersionUID = 6524560251526772839L;

	private Long id;
	private Date data;
	private String description;
	private String locale;
	private Date createDate;
	private Date updateDate;
	private PunchEnum type;
	private Worker worker;
	
	public Punch() {
	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "locale", nullable = true)
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	public PunchEnum getType() {
		return type;
	}

	public void setType(PunchEnum type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
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
		return "Lancamento [id=" + id + ", data=" + data + ", descricao=" + description + ", localizacao=" + locale
				+ ", dataCriacao=" + createDate + ", dataAtualizacao=" + updateDate + ", tipo=" + type
				+ ", funcionario=" + worker + "]";
	}
	
	

}
