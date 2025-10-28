package collector.engine.port

import collector.engine.port.dto.CrawlingResult
import collector.engine.command.CollectCommand

interface Crawler {

    fun crawl(collectCommand: CollectCommand): CrawlingResult
}