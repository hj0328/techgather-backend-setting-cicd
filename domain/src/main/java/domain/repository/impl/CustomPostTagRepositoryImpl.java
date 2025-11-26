package domain.repository.impl;

import application.generator.SnowFlake;
import domain.repository.CustomPostTagRepository;
import domain.vo.PostTagPair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomPostTagRepositoryImpl implements CustomPostTagRepository {

	private final JdbcTemplate jdbcTemplate;
	private final SnowFlake snowflake = SnowFlake.getInstance();

	private static final int BATCH_SIZE = 100;

	private static final String INSERT_SQL = """
			INSERT IGNORE INTO techgather.post_tag (id, post_id, tag_id)
			SELECT ?, p.post_id, t.id
			FROM (SELECT ? AS url) AS input
			INNER JOIN techgather.post p ON p.url = input.url
			INNER JOIN techgather.tag t ON t.name = ?
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
		List<PostTagPair> uniquePairs = pairs.stream()
				.distinct()
				.toList();
		saveBatch(uniquePairs);
	}

	private List<PostTagPair> createPostTagPairs(List<String> urls, List<String> tagNames) {
		List<PostTagPair> pairs = new ArrayList<>(urls.size());
		for (int i = 0; i < urls.size(); i++) {
			pairs.add(PostTagPair.of(urls.get(i), tagNames.get(i)));
		}
		return pairs;
	}

	private void saveBatch(List<PostTagPair> pairs) {
		for (int i = 0; i < pairs.size(); i += BATCH_SIZE) {
			int endIndex = Math.min(i + BATCH_SIZE, pairs.size());
			List<PostTagPair> batch = pairs.subList(i, endIndex);

			List<Long> ids = new ArrayList<>(batch.size());
			for (int j = 0; j < batch.size(); j++) {
				ids.add(snowflake.nextId());
			}

			jdbcTemplate.execute(INSERT_SQL, (java.sql.PreparedStatement ps) -> {
				for (int j = 0; j < batch.size(); j++) {
					PostTagPair pair = batch.get(j);
					ps.setLong(1, ids.get(j));
					ps.setString(2, pair.url());
					ps.setString(3, pair.tagName());
					ps.addBatch();
				}
				return ps.executeBatch();
			});
		}
	}
}

