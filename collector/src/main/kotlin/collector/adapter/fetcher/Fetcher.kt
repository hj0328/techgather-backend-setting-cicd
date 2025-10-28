package collector.adapter.fetcher

import collector.engine.port.dto.CrawlingResult

interface Fetcher {
    fun fetch(urls: List<String>): List<CrawlingResult>

    fun fetch(url: String): CrawlingResult
}