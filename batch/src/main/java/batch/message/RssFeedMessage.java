package batch.message;

import java.time.LocalDateTime;
import java.util.List;

public record RssFeedMessage(String title,
							 String url,
							 LocalDateTime pubDate,
							 List<String> tags,
							 String description,
							 String thumbnail) {

}