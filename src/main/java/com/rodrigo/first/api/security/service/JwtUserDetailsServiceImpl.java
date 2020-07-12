package com.rodrigo.first.api.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rodrigo.first.api.entities.Worker;
import com.rodrigo.first.api.security.JwtUserFactory;
import com.rodrigo.first.api.services.WorkerService;



@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private WorkerService workerService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Worker> worker = workerService.findByEmail(username);

		if (worker.isPresent()) {
			return JwtUserFactory.create(worker.get());
		}

		throw new UsernameNotFoundException("Email não encontrado.");
	}

}