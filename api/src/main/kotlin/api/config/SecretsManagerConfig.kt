package api.config


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import com.fasterxml.jackson.module.kotlin.readValue

@Component
class SecretsManagerConfig(
    @Value("\${aws.secrets-manager.rds-secret-name}")
    private val secretName: String
) {
    private val client = SecretsManagerClient.builder()
        .region(Region.AP_NORTHEAST_2)
        .build()

    private val mapper = jacksonObjectMapper()

    fun getRdsSecrets(): Map<String, String> {
        val request = GetSecretValueRequest.builder()
            .secretId(secretName)
            .build()

        val response = client.getSecretValue(request)
        val secretJson = response.secretString()

        return mapper.readValue(secretJson)
    }
}
