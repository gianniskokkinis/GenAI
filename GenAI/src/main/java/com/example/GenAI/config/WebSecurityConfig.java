package com.example.GenAI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.GenAI.Services.UserServiceImpl;




/*
 * @Configuration Indicates that a class declares one or more 
 * @Bean methods and may be processed by the 
 * Spring container to generate bean definitions 
 * and service requests for those beans at runtime. 
 * The class may also have code that configures other 
 * spring functionalities. 
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	/*
	 * 
	 * Authentication configuration
	 * 
	 */
	
    @Autowired
    private CustomSecuritySuccessHandler customSecuritySuccessHandler;
    
	@Bean 
	public UserDetailsService userDetailsService() { 
		 return new UserServiceImpl(); 
	}
	 
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder(); 
	}
    
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /*
     * DaoAuthenticationProvider is an AuthenticationProvider implementation that uses 
     * a UserDetailsService 
     * and PasswordEncoder 
     * to authenticate a username and password.
     */
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /*
     * Authorization configuration .... 
     */
    
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
                http.authorizeHttpRequests(
                		(authz) -> authz
                		.requestMatchers("/", "/login", "/register", "/save").permitAll()
                        .anyRequest().authenticated()
                		);
                
                http.formLogin(fL -> fL.loginPage("/login")
                		.failureUrl("/login?error=true")
                        .successHandler(customSecuritySuccessHandler)
                        .usernameParameter("username")
                        .passwordParameter("password"));
                
                http.logout(logOut -> logOut.logoutUrl("/logout")
                		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                		.logoutSuccessUrl("/"));
                
                //for upload files
                http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/upload"));
                
                http.authenticationProvider(authenticationProvider());

                return http.build();
    }
}