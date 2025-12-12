package authentication.config.aws;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DbSecretProperties {
    private String username;
    private String password;
    private String url;
    private String endpoint;
    private String database;
}