package com.mvp.vending.config;

import com.mvp.vending.security.JwtSecurityConfigurer;
import com.mvp.vending.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SpringSecurtyConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui**/",
            "/webjars/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui**/",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers("/vending/user/signin").permitAll()
                .antMatchers("/vending/user/signup").permitAll()
                .antMatchers("/vending/user/logout/**").permitAll()
                .antMatchers("/vending/product/all").permitAll()
                .antMatchers("/vending/product/add").hasRole("SELLER")
                .antMatchers("/vending/product/update**").hasRole("SELLER")
                .antMatchers("/vending/product/remove**").hasRole("SELLER")
                .antMatchers("/vending/user/buy**").hasRole("BUYER")
                .antMatchers("/vending/user/reset**").hasRole("BUYER")
                .antMatchers("/vending/user/deposit**").hasRole("BUYER")
                .antMatchers("/vending/user/**").authenticated()
                .antMatchers("/vending/product/**").authenticated()
                .antMatchers("/vending/transaction/**").authenticated()
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    }

}
