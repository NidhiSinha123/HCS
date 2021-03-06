package com.cg.healthcaresystemrest.config;
/*
 * Author:			Jayesh Gaur
 * Description: 	Security configuration class. Responsible for injecting the AuthenticationManagerBuilder
 * Created on: 		October 14, 2019
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				
		.authorizeRequests().antMatchers("/authenticate", "/register").permitAll()
		.antMatchers("/finduser").permitAll()
		.antMatchers("/userpage").hasRole("Customer")
		.antMatchers("/adminpage").hasRole("Admin")
		.antMatchers("/getCenters").permitAll()
		.antMatchers("/getTests").permitAll()
		.antMatchers("/addAppointment","/viewAppointments").hasRole("Customer")
		.antMatchers("/addCenter").hasRole("Admin")
		.antMatchers("/removeCenter").hasRole("Admin")
		.antMatchers("/addTest").hasRole("Admin")
		.antMatchers("/removeTest").hasRole("Admin")
		//	.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
		// all other requests need to be authenticated
		.anyRequest().authenticated().and()
		// make sure we use stateless session; session won't be used to
		// store user's state.
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
		.and()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		/*.authorizeRequests().antMatchers("/authenticate","/register").
		permitAll()
		.antMatchers("/**").permitAll()
		
				
				.antMatchers("/userpage").hasRole("Customer")
				.antMatchers("/addCenter").hasRole("Admin")
				.antMatchers("/adminpage","/addTest","/removeTest","/removeCenter")
				// .permitAll()
				
				 .hasRole("Admin")
				 
				.antMatchers("/getCenters")
				//.permitAll()
				.hasAnyRole("Customer","Admin")
				.antMatchers("/getTests")
				//.permitAll()
				.hasAnyRole("Customer","Admin")
				.antMatchers("/addAppointment")
				//.permitAll()
				 .hasRole("Customer")
				.antMatchers("/viewAppointments")
				//.permitAll()
				.hasRole("Customer")
				.antMatchers("/pendingAppointments")
				//.permitAll()
				.hasRole("Admin")
				.antMatchers("/approveAppointment")
				//.permitAll()
				.hasRole("Admin")			
				.antMatchers("/authenticate", "/register").permitAll()
				.antMatchers("/download").permitAll()
				.antMatchers("/uploadtest").permitAll()
		
		
				// all other requests need to be authenticated
				.anyRequest().authenticated()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}