package com.bootcamp.ondemandreservation.security;

import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
public class ODRAuthProvider {
    @Autowired
    protected ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    protected ODRUserService odrUserService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setPasswordEncoder(odrPasswordEncoder.defaultPasswordEncoder());
        provider.setUserDetailsService(odrUserService);
        return provider;
    }
}
