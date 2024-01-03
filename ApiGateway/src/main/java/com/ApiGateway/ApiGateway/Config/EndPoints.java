package com.ApiGateway.ApiGateway.Config;
import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class EndPoints {
    
    public static final List<String> publicEndPoints = List.of("/auth/login","/eureka");

    public Predicate<ServerHttpRequest> isPublic = serverHttpRequest -> publicEndPoints.stream()
                         .noneMatch(publicUris -> serverHttpRequest.getURI().getPath().contains(publicUris));
                
}
