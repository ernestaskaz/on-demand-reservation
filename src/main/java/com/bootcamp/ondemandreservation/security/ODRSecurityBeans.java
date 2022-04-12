package com.bootcamp.ondemandreservation.security;

import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
public class ODRSecurityBeans {
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

    @Bean
    public AntiSamy antiSamy(final ApplicationContext ctx) {
        try {
            //TODO: get rid of hardcoded URL
            final Policy policy = Policy.getInstance(ctx.getResource("file:src/main/resources/antisamy-policy.xml").getFile());
            return new AntiSamy(policy);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
