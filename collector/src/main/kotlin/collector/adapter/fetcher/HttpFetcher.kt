package collector.adapter.fetcher

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class HttpFetcher: Fetcher {

    private val client = HttpClient(CIO) {
        expectSuccess = false
        engine {
            requestTimeout = 30000
            endpoint {
                connectTimeout = 30000
                socketTimeout = 30000
            }
        }
    }

    override suspend fun fetch(urls: List<String>): List<String> = coroutineScope {
        urls.map { url ->
            async {
                fetch(url)
            }
        }.awaitAll()
    }

    override suspend fun fetch(url: String): String {
        val response: HttpResponse = client.get(url)
        return response.bodyAsText()
    }
}