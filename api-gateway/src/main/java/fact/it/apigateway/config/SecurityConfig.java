package fact.it.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    String[] PublicEndpoints = {
            "/public/bnb", "/public/room/available", "/public/room", "/test/**"
    };
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
            .authorizeExchange(exchange ->
                exchange.pathMatchers(HttpMethod.GET, PublicEndpoints)
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(withDefaults())
            );
        return serverHttpSecurity.build();
    }
}

