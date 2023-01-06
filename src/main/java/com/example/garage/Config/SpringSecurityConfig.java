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
                .antMatchers(HttpMethod.GET, "/cars").permitAll()
                .antMatchers(HttpMethod.GET, "/cars/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/cars/licenseplate/{licenseplate}").permitAll()
                .antMatchers(HttpMethod.GET, "/cars/user").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/cars/user/status").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/cars").hasRole("DESK")
                .antMatchers(HttpMethod.PUT, "/cars/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/cars/{id}/statusdesk").hasRole("DESK")
                .antMatchers(HttpMethod.PUT, "/cars/{id}/statusmechanic").hasRole("MECHANIC")
                .antMatchers(HttpMethod.DELETE,"/cars/{id}").permitAll()

                //----------------------------------------Endpoints Invoice--------------------------------------
                .antMatchers(HttpMethod.GET, "/invoices").permitAll()
                .antMatchers(HttpMethod.GET, "/invoices/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/invoices/user").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/invoices/{id}/getpdfinvoice").permitAll()
                .antMatchers(HttpMethod.POST, "/invoices/{service_id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/payed").hasRole("DESK")
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/generateInvoicePdf").permitAll()
                .antMatchers(HttpMethod.PUT, "/invoices/{id}/sendinvoice").permitAll()
                .antMatchers(HttpMethod.DELETE, "/invoices/{id}").permitAll()


                //----------------------------------------Endpoints Service--------------------------------------
                .antMatchers(HttpMethod.GET, "/services").permitAll()
                .antMatchers(HttpMethod.GET, "/services/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/services/user").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/services/{car_id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/services/{id}/mechanicdone").permitAll()
                .antMatchers(HttpMethod.PUT, "/services/{id}/approvaluser").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/services/{id}").permitAll()


                //----------------------------------------Endpoints Repair--------------------------------------
                .antMatchers(HttpMethod.GET, "/repairs/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/repairs/{carpart_id}/{service_id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/repairs/{id}/setrepaired").hasRole("MECHANIC")
                .antMatchers(HttpMethod.DELETE, "/repairs/{id}").permitAll()


                //----------------------------------------Endpoints User--------------------------------------
                .antMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/users/{username}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/users/{username}/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/users/{username}/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/users/{username}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/users/{username}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/users/{username}/authorities/{authority}").hasRole("ADMIN")

                //----------------------------------------Endpoints Repair--------------------------------------
                .antMatchers(HttpMethod.GET, "/carparts/{car_id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/carparts/{car_id}/inspection/{carpart}").permitAll()

                //----------------------------------------Endpoints Repair--------------------------------------
                .antMatchers(HttpMethod.GET, "/carpapers/upload/{user_id}").permitAll()

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