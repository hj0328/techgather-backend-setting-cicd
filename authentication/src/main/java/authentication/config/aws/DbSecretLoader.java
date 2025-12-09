package authentication.config.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Component
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
            // 수정 필요
            throw new IllegalStateException("Failed to load secret configuration", e);
        }
    }
}
