package com.example.garage.Config;

import com.example.garage.Filter.JwtRequestFilter;
import com.example.garage.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }



    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                // Wanneer je deze uncomments, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                .antMatchers("/**").permitAll()
                //----------------------------------------Endpoints Car--------------------------------------
                .antMatchers(HttpMethod.GET, "/cars").hasAnyRole("DESK","MECHANIC","ADMIN")
                .antMatchers(HttpMethod.GET, "/cars/{id}").hasAnyRole("DESK","MECHANIC","ADMIN")
                .antMatchers(HttpMethod.GET, "/cars/licenseplate/{licenseplate}").hasAnyRole("DESK","MECHANIC","ADMIN")
                .antMatchers(HttpMethod.GET, "/cars/user").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/cars/user/status").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/cars").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/cars/statusdesk/{carstatus}/{licenseplate}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/cars/statusmechanic/{carstatus}/{licenseplate}").hasAnyRole("MECHANIC","ADMIN")
                .antMatchers(HttpMethod.PUT, "/cars/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/cars/{id}").hasAnyRole("DESK","ADMIN")

                //----------------------------------------Endpoints Invoice--------------------------------------
                .antMatchers(HttpMethod.GET, "/invoices").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.GET, "/invoices/{id}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.GET, "/invoices/user").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/invoices/{id}/getpdfinvoice").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.POST, "/invoices/{service_id}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/payed").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/generateInvoicePdf").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/sendinvoice").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/invoices/{id}").hasRole("ADMIN")

                //----------------------------------------Endpoints Service--------------------------------------
                .antMatchers(HttpMethod.GET, "/maintenances").hasAnyRole("DESK","MECHANIC","ADMIN")
                .antMatchers(HttpMethod.GET, "/maintenances/{id}").hasAnyRole("DESK","MECHANIC","ADMIN")
                .antMatchers(HttpMethod.GET, "/maintenances/user").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/maintenances/{car_id}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.PUT, "/maintenances/{id}/mechanicdone").hasAnyRole("MECHANIC","ADMIN")
                .antMatchers(HttpMethod.PUT, "/maintenances/{id}/approvaluser").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/maintenances/{id}").hasRole("ADMIN")

                //----------------------------------------Endpoints Repair--------------------------------------
                .antMatchers(HttpMethod.GET, "/repairs/{licenseplate}").hasAnyRole("MECHANIC","DESK","ADMIN")
                .antMatchers(HttpMethod.POST, "/repairs/{carpart_id}/{service_id}").hasAnyRole("MECHANIC","ADMIN")
                .antMatchers(HttpMethod.PUT, "/repairs/{id}/setrepaired").hasAnyRole("MECHANIC","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/repairs/{id}").hasAnyRole("MECHANIC","ADMIN")

                //----------------------------------------Endpoints User--------------------------------------
                .antMatchers(HttpMethod.GET,"/users").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.GET,"/users/{username}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.GET,"/users/{username}/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/users/{username}/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/users/{username}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/users/{username}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/users/{username}/authorities/{authority}").hasRole("ADMIN")

                //----------------------------------------Endpoints Carparts--------------------------------------
                .antMatchers(HttpMethod.GET, "/carparts/{licenseplate}").hasAnyRole("MECHANIC","ADMIN")
                .antMatchers(HttpMethod.PUT, "/carparts/{licenseplate}/inspection/{carpart}").hasAnyRole("MECHANIC","ADMIN")

                //----------------------------------------Endpoints Carpapers--------------------------------------
                .antMatchers(HttpMethod.GET, "/carpapers/getpdfcarpapers/{licenseplate}").hasAnyRole("DESK","ADMIN")
                .antMatchers(HttpMethod.POST, "/carpapers/upload/{user_id}").hasAnyRole("MECHANIC","ADMIN","DESK","USER")

                //----------------------------------------Endpoints Auth--------------------------------------
                .antMatchers(HttpMethod.GET,"/authenticated").authenticated()
                .antMatchers(HttpMethod.POST,"/authenticate").permitAll()
                //.antMatchers("/cimodules", "/remotecontrollers", "/televisions", "/wallbrackets").hasAnyRole("ADMIN", "USER")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}