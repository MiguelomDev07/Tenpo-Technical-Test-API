package cl.tenpo.test.components.impl;

import cl.tenpo.test.components.ApiRequestComponent;
import cl.tenpo.test.config.WebClientBaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiRequestComponentImpl implements ApiRequestComponent {

    @Autowired
    private WebClientBaseConfig webClientBaseConfig;

    @Override
    public <T> T get(Class<T> classToMappingResponse, String apiUrl) {
        var client = webClientBaseConfig.webClientBase();
        return client.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(classToMappingResponse)
                .block();
    }
}
