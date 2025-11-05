package collector.adapter.publisher.kafka

import collector.engine.port.Publisher
import collector.engine.model.Message
import org.springframework.stereotype.Component

@Component
class KafkaPublisher: Publisher {
    override fun publish(messages: List<Message>) {
        messages.forEach {
            println(it)
        }
    }
}