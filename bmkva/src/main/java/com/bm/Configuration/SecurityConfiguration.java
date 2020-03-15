package com.bm.Configuration;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends  WebSecurityConfigurerAdapter {
	
	
	 @Override
	 protected void configure(HttpSecurity http) throws Exception{
		 http.authorizeRequests()
		// .antMatchers("/login").permitAll()
		 .antMatchers("/").permitAll()
		 .antMatchers("/signup").permitAll()
		 .antMatchers("/confirm").permitAll()
		 .antMatchers("/forgotpassword").permitAll()
		 .antMatchers("/resetpassword").permitAll()
		 .antMatchers("/view").permitAll()
		 
			 
		 .antMatchers("/add").permitAll()
		
		   .and().logout()
		   .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		   .logoutSuccessUrl("/");
		   
		 
		 
	 

}
}
