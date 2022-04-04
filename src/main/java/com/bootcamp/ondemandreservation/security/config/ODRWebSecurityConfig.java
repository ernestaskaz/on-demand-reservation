package com.bootcamp.ondemandreservation.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ODRWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log= LoggerFactory.getLogger(ODRWebSecurityConfig.class);
    @Value("${security.csrf}")
    private boolean csrfEnabled;

    //private final PasswordEncoder appPasswordEncoder;

    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRWebSecurityConfig() {
    }

    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRWebSecurityConfig(boolean csrfEnabled/*, PasswordEncoder appPasswordEncoder*/) {
        this.csrfEnabled = csrfEnabled;
        //this.appPasswordEncoder = appPasswordEncoder;
    }

    /**
     * Creates an instance which allows specifying if the default configuration should be
     * enabled. Disabling the default configuration should be considered more advanced
     * usage as it requires more understanding of how the framework is implemented.
     *
     * @param disableDefaults true if the default configuration should be disabled, else
     *                        false
     */
    public ODRWebSecurityConfig(boolean disableDefaults, boolean csrfEnabled/*, PasswordEncoder appPasswordEncoder*/) {
        //super(disableDefaults);
        this.csrfEnabled = csrfEnabled;
        //this.appPasswordEncoder = appPasswordEncoder;
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
            log.trace("CSRF should be enabled");
        }else{
            http.csrf().disable();
            log.warn("CSRF protection disabled for testing purposes, re-enable in production.");
        }
        http.authorizeRequests().antMatchers("/**").permitAll();
    }
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        provider.setUserDetailsService(odrUserService);
        return provider;
    }
*/

}
