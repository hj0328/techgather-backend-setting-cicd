package collector.adapter.crawler

import collector.adapter.fetcher.HttpFetcher
import collector.engine.port.dto.CrawlingResult
import collector.engine.command.CollectCommand
import collector.engine.port.Crawler
import org.springframework.stereotype.Component

@Component
class HtmlCrawler(
    private val httpFetcher: HttpFetcher,
): Crawler {
    override fun crawl(collectCommand: CollectCommand): CrawlingResult {

        TODO("Not yet implemented")
        //html 파일 가져오기
//        return httpFetcher.fetch(collectCommand.url)
    }
}