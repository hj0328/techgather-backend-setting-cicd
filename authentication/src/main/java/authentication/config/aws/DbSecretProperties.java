package authentication.config.aws;

public record DbSecretProperties(
        String username,
        String password,
        String url
) {}