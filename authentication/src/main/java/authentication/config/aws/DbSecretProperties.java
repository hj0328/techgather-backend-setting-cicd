package authentication.config.aws;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DbSecretProperties {
    private String username;
    private String password;
    private String url;
}