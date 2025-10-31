package collector.engine

import collector.engine.port.Publisher
import collector.engine.port.Extractor
import collector.engine.command.CollectCommand
import collector.engine.port.Crawler
import collector.engine.port.DeduplicatePort

class CollectEngine(
    private val crawler: Crawler,
    private val extractor: Extractor,
    private val deduplicatePort: DeduplicatePort,
    private val publisher: Publisher,
) {
    fun run(command: CollectCommand) {

        // 크롤링 후 message 추출
        val crawlingResult = crawler.crawl(command)

        //messages 추출
        val messages = extractor.extract(crawlingResult, command.extractCommand)

        //중복 제거
        val deduplicatedMessages = deduplicatePort.deduplicate(messages)

        //메시지 publish
        publisher.publish(deduplicatedMessages)
    }
}