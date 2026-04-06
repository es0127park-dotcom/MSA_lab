package com.metacoding.order.core.config;

import org.springframework.context.annotation.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.*;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        ClientHttpRequestInterceptor authForwardingInterceptor = (request, body, execution) -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String authorization = attributes.getRequest().getHeader("Authorization");
                if (authorization != null) {
                    request.getHeaders().add("Authorization", authorization);
                }
            }
            return execution.execute(request, body);
        };

        return RestClient.builder().requestInterceptor(authForwardingInterceptor);
    }
}
