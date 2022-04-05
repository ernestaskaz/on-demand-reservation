package com.bootcamp.ondemandreservation.security.config;

import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity only needed if this loads 1st
@Configuration
@Order(2)
public class ODRRESTSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log= LoggerFactory.getLogger(ODRRESTSecurityConfig.class);
    @Value("${security.csrf}")
    protected boolean csrfEnabled;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRRESTSecurityConfig() {
    }

    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRRESTSecurityConfig(boolean csrfEnabled, AuthenticationProvider authenticationProvider) {
        this.csrfEnabled = csrfEnabled;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     * <p>
     * Any endpoint that requires defense against common vulnerabilities can be specified
     * here, including public ones. See {@link HttpSecurity#authorizeRequests} and the
     * `permitAll()` authorization rule for more details on public endpoints.
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(csrfEnabled){
            log.trace("API CSRF should be enabled");
        }else{
            http.csrf().disable();
            log.warn("API CSRF protection disabled for testing purposes, re-enable in production.");
        }
        //http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic();

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }



}
