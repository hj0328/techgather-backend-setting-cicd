package authentication.config.aws;

public record DbSecretProperties(
        String dbUsername,
        String dbPassword,
        String dbUrl
) {}