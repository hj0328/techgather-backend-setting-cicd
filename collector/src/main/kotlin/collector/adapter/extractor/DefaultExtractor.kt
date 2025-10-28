package collector.adapter.extractor

import collector.engine.port.dto.CrawlingResult
import collector.engine.model.Message
import collector.engine.port.Extractor

class DefaultExtractor: Extractor {
    override fun extract(crawlingResult: CrawlingResult): List<Message> {
        TODO("Not yet implemented")
    }
}