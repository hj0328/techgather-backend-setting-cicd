package collector.adapter.fetcher

import collector.engine.port.dto.CrawlingResult
import org.springframework.stereotype.Component

@Component
class HttpFetcher: Fetcher {

    override fun fetch(urls: List<String>): List<CrawlingResult> {
        TODO("Not yet implemented")
    }

    override fun fetch(url: String): CrawlingResult {
        TODO("Not yet implemented")
    }
}