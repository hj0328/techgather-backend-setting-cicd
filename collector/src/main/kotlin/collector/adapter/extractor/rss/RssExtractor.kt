package collector.adapter.extractor.rss

import collector.adapter.extractor.thumbnail.ThumbnailDownloader
import collector.engine.command.ExtractCommand
import collector.engine.model.Message
import collector.engine.port.Extractor
import collector.engine.port.dto.CrawlingResult
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Component

@Component
class RssExtractor(
    private val thumbnailDownloader: ThumbnailDownloader,
): Extractor {

    private val xmlMapper = XmlMapper().findAndRegisterModules()

    override fun extract(crawlingResult: CrawlingResult, extractCommand: ExtractCommand): List<Message> {

        val rss = xmlMapper.readValue(crawlingResult.body, Rss::class.java)

        return rss.channel.items?.map {
            Message(
                title = it.title,
                url = it.link,
                pubDate = it.pubDate,
                tags = it.categories ?: listOf(),
                description = it.description,
                thumbnail = getThumbnail(it.link, extractCommand)
            )
        }?.toList() ?: emptyList()
    }

    private fun getThumbnail(url: String, command: ExtractCommand): String? {

        if(command.useDefaultThumbnail) return command.defaultThumbnail

        return thumbnailDownloader.download(url) ?: command.defaultThumbnail
    }
}