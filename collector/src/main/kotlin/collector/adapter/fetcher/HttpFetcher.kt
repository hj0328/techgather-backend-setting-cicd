package collector.adapter.fetcher

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class HttpFetcher: Fetcher {

    private val client = HttpClient(CIO) {
        expectSuccess = false
        engine {
            requestTimeout = 5000
        }
    }

    override fun fetch(urls: List<String>): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetch(url: String): String = runBlocking {
        val response: HttpResponse = client.get(url)
        response.bodyAsText()
    }
}