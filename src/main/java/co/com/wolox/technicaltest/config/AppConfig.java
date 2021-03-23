package co.com.wolox.technicaltest.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${urlExternal}")
    private String url;

    @Bean("clientRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri(url).build();
    }

    @Bean
    public ModelMapper modelMapper() { return new ModelMapper(); }
}
