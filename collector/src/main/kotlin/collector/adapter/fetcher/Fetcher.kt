package collector.adapter.fetcher

interface Fetcher {
    fun fetch(urls: List<String>): List<String>

    fun fetch(url: String): String
}