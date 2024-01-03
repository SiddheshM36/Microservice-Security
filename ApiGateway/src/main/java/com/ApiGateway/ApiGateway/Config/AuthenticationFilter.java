package com.ApiGateway.ApiGateway.Config;

import org.bouncycastle.oer.its.ieee1609dot2.basetypes.PublicEncryptionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private EndPoints endPoints;

     @Autowired
    private JWTConfig jwtConfig;


    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain)->{
            if(endPoints.isPublic.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Please provide token");
                }
            }
                String jwtTokenWithBearer = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwtToken = null;

                if(jwtTokenWithBearer !=null && jwtTokenWithBearer.startsWith("Bearer ")){
                    jwtToken = jwtTokenWithBearer.substring(7);
                }

                jwtConfig.getClaims(jwtToken);
                System.out.println("Token information is " + jwtConfig.getClaims(jwtToken));
                return chain.filter(exchange);
            
        });
    }

//     The above filter checks if the request is public using the endPoints.isPublic predicate. 
// If so, 
// it ensures that an Authorization header is present; otherwise, it throws a RuntimeException.
// It extracts the JWT token from the Authorization header, if present, and removes the "Bearer " prefix.
// The getClaims method from jwtConfig is called to process the JWT token.
// Finally, the filter calls chain.filter(exchange) to continue the filter chain

    public static class Config {
        // Define configuration properties and methods here
        // For example:
        // private String someConfigValue;
        // public String getSomeConfigValue() { return someConfigValue; }
        // public void setSomeConfigValue(String someConfigValue) { this.someConfigValue = someConfigValue; }
    }
}




