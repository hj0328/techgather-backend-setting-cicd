package batch.writer;

import batch.message.RssFeedMessage;
import domain.entity.Post;
import domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class RssFeedWriter implements ItemWriter<RssFeedMessage> {

    private final PostRepository postRepository;

    @Override
    public void write(Chunk<? extends RssFeedMessage> chunk) {
        if (chunk.isEmpty()) {
            return;
        }

        try {
            Map<String, RssFeedMessage> urlMap = chunk.getItems().stream()
                    .collect(Collectors.toMap(
                            RssFeedMessage::url,
                            msg -> msg,
                            (existing, replacement) -> existing
                    ));

            List<String> urls = List.copyOf(urlMap.keySet());
            List<String> existingUrls = postRepository.findUrlsByUrlIn(urls);

            var newMessages = urlMap.values().stream()
                    .filter(msg -> !existingUrls.contains(msg.url()))
                    .toList();

            List<Post> posts = newMessages.stream()
						.map(message -> Post.builder()
						.title(message.title())
						.url(message.url())
						.pubDate(message.pubDate())
						.tags(message.tags() != null ? message.tags() : List.of())
						.description(message.description())
						.thumbnail(message.thumbnail())
						.build())
						.toList();
            postRepository.saveAll(posts);
        } catch (Exception e) {
            throw new RuntimeException(e);	//TODO application 모듈 서 응답 스펙 정의되면 다시 수정해야합니다.
        }
    }
}