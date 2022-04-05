package com.bootcamp.ondemandreservation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ODRPasswordEncoder {
    /**
     * We use the default DelegatingPasswordEncoder now.
     * Can possibly create some issues if it's changed, but that probably means the old one was
     * compromised anyway, so eveyone needs to change their passwords for that reason.
     */
    @Bean
    public PasswordEncoder defaultPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}