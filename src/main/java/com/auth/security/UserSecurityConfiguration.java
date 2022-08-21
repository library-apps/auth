package com.auth.security;

import com.auth.filter.CustomAuthenticationFilter;
import com.auth.filter.CustomAuthorizationFilter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private org.springframework.security.core.userdetails.UserDetailsService UserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(UserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());

        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**", "/api/v1/**").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**", "/v2/auth/info").hasAnyAuthority("ROLE_USER",
//                "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority("ROLE_MANAGER",
//                "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/role/**").hasAnyAuthority("ROLE_MANAGER",
//                "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    @SneakyThrows(Exception.class)
    public AuthenticationManager authenticationManagerBean() {

        return super.authenticationManagerBean();
    }
}
