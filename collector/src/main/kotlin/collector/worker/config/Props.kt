package collector.worker.config

import org.springframework.boot.context.properties.ConfigurationProperties

data class AdapterProps(
    var fetcher: String = "",
    var crawler: String = "",
    var extractor: String = "",
)
data class TargetProps(
    var url: String = "",
    var adapter: AdapterProps = AdapterProps()
)

@ConfigurationProperties(prefix = "target")
class TargetProperties(
    var entries: Map<String, TargetProps> = emptyMap()
)