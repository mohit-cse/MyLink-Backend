package com.mohit.apigateway.Controllers;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class SwaggerController {
    @GetMapping("/v3/api-docs/swagger-config")
    public Map<String, Object> swaggerConfig(ServerHttpRequest serverHttpRequest) throws URISyntaxException {
        URI uri = serverHttpRequest.getURI();
        String url = new URI(uri.getScheme(), uri.getAuthority(), null, null, null).toString();
        Map<String, Object> swaggerConfig = new LinkedHashMap<>();
        List<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = new LinkedList<>();
        String docService = "doc-service";
        String userService = "user-service";
        swaggerUrls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl("MyLink",
                url + "/" + userService + "/v3/api-docs",
                    userService));
        swaggerUrls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl("MyLink",
                url + "/" + docService + "/v3/api-docs",
                    docService));
        swaggerConfig.put("urls", swaggerUrls);
        return swaggerConfig;
    }
}
