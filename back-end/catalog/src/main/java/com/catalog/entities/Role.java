package com.catalog.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = -8416218186248592841L;

	@Id
    private int id;
    
    @Column(name = "authority", nullable = false)
    private String authority;
  
}
