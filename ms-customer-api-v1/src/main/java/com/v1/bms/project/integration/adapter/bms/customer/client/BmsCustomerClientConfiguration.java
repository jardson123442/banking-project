package com.v1.bms.project.integration.adapter.bms.customer.client;

import com.v1.bms.project.domain.vo.exceptions.BmsApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.nio.charset.StandardCharsets;

@Configuration
public class BmsCustomerClientConfiguration {

    @Bean
    BmsCustomerClient bmsCustomerClient(
            @Value("${integration.customer-bms.base-url}") String baseUrl) {
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(
                        status -> status.isError(),
                        (request, response) -> {
                            String responseBody = new String(
                                    response.getBody().readAllBytes(),
                                    StandardCharsets.UTF_8);
                            throw new BmsApiException(
                                    response.getStatusCode(),
                                    responseBody);
                        })
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(BmsCustomerClient.class);
    }
}
