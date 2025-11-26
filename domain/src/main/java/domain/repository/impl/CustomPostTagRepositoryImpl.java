package domain.repository.impl;

import domain.repository.CustomPostTagRepository;
import domain.vo.PostTagPair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomPostTagRepositoryImpl implements CustomPostTagRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final int BATCH_SIZE = 100;

	private static final String INSERT_SQL = """
			INSERT IGNORE INTO techgather.post_tag (post_id, tag_id)
			SELECT p.post_id, t.id
			FROM techgather.post p
			INNER JOIN techgather.tag t
			WHERE p.url = ? AND t.name = ?
			""";

	public CustomPostTagRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void saveAllUrlAndTag(List<String> urls, List<String> tagNames) {
		if (urls == null || urls.isEmpty() || tagNames == null || tagNames.isEmpty()) {
			return;
		}

		List<PostTagPair> pairs = createPostTagPairs(urls, tagNames);
		saveBatch(pairs);
	}

	private List<PostTagPair> createPostTagPairs(List<String> urls, List<String> tagNames) {
		List<PostTagPair> pairs = new ArrayList<>(urls.size() * tagNames.size());
		for (String url : urls) {
			for (String tagName : tagNames) {
				pairs.add(PostTagPair.of(url, tagName));
			}
		}
		return pairs;
	}

	private void saveBatch(List<PostTagPair> pairs) {
		for (int i = 0; i < pairs.size(); i += BATCH_SIZE) {
			List<PostTagPair> batch = pairs.subList(i, Math.min(i + BATCH_SIZE, pairs.size()));

			jdbcTemplate.batchUpdate(INSERT_SQL, batch, batch.size(), (ps, pair) -> {
				ps.setString(1, pair.url());
				ps.setString(2, pair.tagName());
			});
		}
	}
}

