package ca.sheridancollege.waamande.security;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {

		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);

		List<GrantedAuthority> authUser = new ArrayList<GrantedAuthority>();
		authUser.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

		List<GrantedAuthority> authManager = new ArrayList<GrantedAuthority>();
		authManager.add(new SimpleGrantedAuthority("ROLE_MANAGER"));

		String encodedPassword1 = new BCryptPasswordEncoder().encode("pass1");
		String encodedPassword2 = new BCryptPasswordEncoder().encode("pass2");
		String encodedPassword3 = new BCryptPasswordEncoder().encode("pass3");

		User u1 = new User("John35", encodedPassword1, authUser);
		User u2 = new User("Mike76", encodedPassword2, authUser);
		User u3 = new User("Jonathon91", encodedPassword3, authManager);

		jdbcUserDetailsManager.createUser(u1);
		jdbcUserDetailsManager.createUser(u2);
		jdbcUserDetailsManager.createUser(u3);
		
		return jdbcUserDetailsManager;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**").and().ignoring().antMatchers("/h2-console/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http
	.authorizeRequests().antMatchers("/register").hasRole("MANAGER")
	.and()
	.authorizeRequests().antMatchers("/threadInput").hasAnyRole("EMPLOYEE", "MANAGER")
	.and()
	.authorizeRequests().antMatchers("/postInput/{id}").hasAnyRole("EMPLOYEE", "MANAGER")
	.and()
	.authorizeRequests().antMatchers("/postInput").hasAnyRole("EMPLOYEE", "MANAGER")
	.and()
	.authorizeRequests().antMatchers("/discussonForum").hasAnyRole("EMPLOYEE", "MANAGER")
	.and()
	.authorizeRequests().antMatchers("/postView/{id}").hasAnyRole("EMPLOYEE", "MANAGER")
	.anyRequest().authenticated()
	.and()
	.formLogin().loginPage("/login").permitAll()
	.and().logout().invalidateHttpSession(true).clearAuthentication(true)
	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	.logoutSuccessUrl("/login?logout").permitAll();
	}
}
