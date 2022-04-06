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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Order(1)
public class ODRWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log= LoggerFactory.getLogger(ODRWebSecurityConfig.class);
    @Value("${security.csrf}")
    private boolean csrfEnabled;
    @Autowired
    private AuthenticationProvider  authenticationProvider;
    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRWebSecurityConfig() {
    }

    /**
     * Creates an instance with the default configuration enabled.
     */
    public ODRWebSecurityConfig(boolean csrfEnabled, AuthenticationProvider authenticationProvider) {
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
            log.trace("CSRF should be enabled");
        }else{
            http.csrf().disable();
            log.warn("CSRF protection disabled for testing purposes, re-enable in production.");
        }
        //http.authorizeRequests().antMatchers("/**").permitAll();
        http.logout()
                .invalidateHttpSession(true).deleteCookies("JSESSIONID").clearAuthentication(true)
                .logoutUrl("/logout_now").logoutSuccessUrl("/logoutSuccess").and()
                .authorizeRequests().antMatchers("/web/**").authenticated().and().formLogin().defaultSuccessUrl("/web/", false);

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }



}
