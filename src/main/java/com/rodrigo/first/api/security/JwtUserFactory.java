package com.rodrigo.first.api.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.enums.RoleEnum;

public class JwtUserFactory {
	private JwtUserFactory() {
	}

	/**
	 * Converte e gera um JwtUser com base nos dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return JwtUser
	 */
	public static JwtUser create(Worker worker) {
		return new JwtUser(worker.getId(), worker.getEmail(), worker.getPassword(),
				mapToGrantedAuthorities(worker.getRole()));
	}

	/**
	 * Converte o perfil do usuário para o formato utilizado pelo Spring Security.
	 * 
	 * @param RoleEnum
	 * @return List<GrantedAuthority>
	 */
	private static List<GrantedAuthority> mapToGrantedAuthorities(RoleEnum roleEnum) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(roleEnum.toString()));
		return authorities;
	}
}
