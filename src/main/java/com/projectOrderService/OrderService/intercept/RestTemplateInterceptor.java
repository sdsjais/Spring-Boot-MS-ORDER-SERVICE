package com.projectOrderService.OrderService.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    public RestTemplateInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager){
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization","Bearer "
                +oAuth2AuthorizedClientManager
                .authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("internal-client")
                        .principal("internal")
                        .build())
                .getAccessToken().getTokenValue());
        return execution.execute(request,body);
    }
}
