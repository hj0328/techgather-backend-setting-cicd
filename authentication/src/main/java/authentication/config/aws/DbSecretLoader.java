package authentication.config.aws;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class DbSecretLoader {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    public DbSecretProperties load(String secretName) {
        try {
            String json = fetchSecret(secretName);
            Map<String, Object> secret = parse(json);

            return DbSecretProperties.builder()
                    .username(requireString(secret, "username"))
                    .password(requireString(secret, "password"))
                    .url(requireString(secret, "url"))
                    .build();

        } catch (Exception e) {
            log.error("Failed to load DB secret. raw cause = {}", e.getMessage(), e);
            throw new IllegalStateException(
                    "Failed to load DB secret: " + secretName, e
            );
        }
    }

    private String fetchSecret(String secretName) {
        GetSecretValueResponse response =
                secretsManagerClient.getSecretValue(
                        GetSecretValueRequest.builder()
                                .secretId(secretName)
                                .build()
                );

        return Optional.ofNullable(response.secretString())
                .orElseThrow(() -> new IllegalStateException(
                        "SecretString is null: " + secretName
                ));
    }

    private Map<String, Object> parse(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    private String requireString(Map<String, Object> map, String key) {
        Object value = map.get(key);

        if (value instanceof String s && !s.isBlank()) {
            return s;
        }

        throw new IllegalStateException(
                "Required secret key missing or invalid: " + key
        );
    }
}
