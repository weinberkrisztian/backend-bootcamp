package com.udemy.springsection2.configuration;

import com.udemy.springsection2.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class ProjectSecurityConfig  extends WebSecurityConfigurerAdapter {

    /**
     * /myAccount - Secured /myBalance - Secured /myLoans - Secured /myCards -
     * Secured /notices - Not Secured /contact - Not Secured
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
        authorizeRequests()
                .antMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
                .antMatchers("/notices", "/contact").permitAll()
                .and()
        .formLogin()
                .and()
        .httpBasic();

       // CONFIGURATION TO DENY ALL THE REQUEST
        /**
        http.authorizeRequests().anyRequest().denyAll().and()
                .formLogin().and().httpBasic();
        */

        // PERMITT ALL REQUEST
        /**
        http.authorizeRequests().anyRequest().permitAll().and()
                .formLogin().and().httpBasic();
         */
    }

    // WE MADE A CUSTOM USER DETAILS SERVICE, THEREFORE SPRING WILL AUTOMATICALLY CONFIGURE IT TO HADNLE THE AUTHENTICATION


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//         implements UserDetailsService
//         in memory

//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        UserDetails admin= User.withUsername("admin").password("12345").authorities("admin").build();
//        UserDetails user= User.withUsername("user").password("12345").authorities("admin").build();
//        inMemoryUserDetailsManager.createUser(admin);
//        inMemoryUserDetailsManager.createUser(user);
//        auth.userDetailsService(inMemoryUserDetailsManager).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        // using default configuration for jdbc manager
        // it read the values from the app prop to connect to the databese
        // we have to follow a given structure in the database with the users and auth table

        return new JdbcUserDetailsManager(dataSource);

    }
    */
}
