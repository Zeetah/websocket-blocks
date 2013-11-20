package com.zeetah.blocks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() //TODO Refactor login form
			.authorizeRequests()
				.antMatchers("/assets/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.logout()
				.logoutSuccessUrl("/login.html?logout")
				.logoutUrl("/logout.html")
				.permitAll()
				.and()
			.formLogin()
				.defaultSuccessUrl("/index.html")
				.successHandler(successHandler())
				.loginPage("/login.html")
				.failureHandler(failureHandler())
				.permitAll();
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new BlocksAuthenticationErrorHandler();
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new BlocksAuthenticationSuccessHandler();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("block").password("block12").roles("USER").and()
				.withUser("socket").password("socket12").roles("USER");
	}
}