package com.cg.healthcaresystemrest.config;
/*
 * Author: 		Jayesh Gaur
 * Description: Audit Trial
 * Created on:  October 12, 2019
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			return Optional.of(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException exception) {
		exception.printStackTrace();
		}
		return null;
	}

}
