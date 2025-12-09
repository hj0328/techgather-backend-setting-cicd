package authentication.config.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class DataSourceConfig {

    private final ObjectMapper mapper;
    private final SecretsManagerClient client;

    @Bean
    public DataSource dataSource() throws JsonProcessingException {
        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId("prod/db/parameter")
                .build();

        GetSecretValueResponse response = client.getSecretValue(request);

        String secretJson = response.secretString();
        JsonNode json = mapper.readTree(secretJson);

        String url = json.get("url").asText();
        String username = json.get("username").asText();
        String password = json.get("password").asText();


        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return new HikariDataSource(config);
    }
}
