package authentication.config.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class DbSecretLoader {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    public DbSecretProperties load(String secretName) {
        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);

        try {
            String json = response.secretString();
            return objectMapper.readValue(json, DbSecretProperties.class);
        } catch (Exception e) {
            // 수정
            throw new IllegalStateException("Failed to parse DB secret", e);
        }
    }
}
