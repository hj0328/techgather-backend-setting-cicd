package api.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import javax.sql.DataSource

@Configuration
@Profile("prod")
class DataSourceConfig(
    private val secretsManagerConfig: SecretsManagerConfig
) {

    @Bean
    fun dataSource(): DataSource {
        val secrets = secretsManagerConfig.getRdsSecrets()
        val jdbcUrl = secrets["url"]!!
        val username = secrets["username"]!!
        val password = secrets["password"]!!

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
        }

        return HikariDataSource(config)
    }
}
