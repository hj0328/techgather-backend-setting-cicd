package domain.repository.impl;

import domain.entity.Tag;
import domain.repository.CustomTagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomTagRepositoryImpl implements CustomTagRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final String BINDING_PARAMETER = "?";
	private static final String DELIMITER = ", ";
	private static final String INSERT_SQL = "INSERT INTO techgather.tag (id, name) VALUES (?, ?) " +
 											 "ON DUPLICATE KEY UPDATE " +
 											 "name = VALUES(name)";

	private static final String SELECT_SQL = "SELECT id, name FROM techgather.tag WHERE name IN (%s)";

	private static final int BATCH_SIZE = 100;

	public CustomTagRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void saveAll(List<Tag> tags) {
		if (tags == null || tags.isEmpty()) {
			return;
		}

		List<String> names = tags.stream()
				.map(Tag::getName)
				.distinct()
				.toList();

		Map<String, Long> tagIds = findTagIdsByNames(names);

		List<Tag> tagList = tags.stream()
				.map(tag -> {
					Long id = tagIds.get(tag.getName());
					if (Objects.nonNull(id)) {
						return Tag.create(id, tag.getName());
					}
					return tag;
				})
				.toList();

		jdbcTemplate.batchUpdate(INSERT_SQL, tagList, tagList.size(), (ps, tag) -> {
			ps.setLong(1, tag.getId());
			ps.setString(2, tag.getName());
		});
	}

	private Map<String, Long> findTagIdsByNames(List<String> names) {
		if (names == null || names.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, Long> map = new HashMap<>();
		for (int i = 0; i < names.size(); i += BATCH_SIZE) {
			List<String> batchNames = extractBatch(names, i);
			Map<String, Long> batchResult = queryTagIdsByNames(batchNames);
			map.putAll(batchResult);
		}

		return map;
	}

	private List<String> extractBatch(List<String> names, int startIndex) {
		int endIndex = Math.min(startIndex + BATCH_SIZE, names.size());
		return names.subList(startIndex, endIndex);
	}

	private Map<String, Long> queryTagIdsByNames(List<String> names) {
		String sql = buildSelectSqlWithInClause(names.size());

		return jdbcTemplate.query(
			sql,
			ps -> bindNameParameters(ps, names),
			rs -> {
				Map<String, Long> result = new HashMap<>();
				while (rs.next()) {
					String name = rs.getString("name");
					Long id = rs.getLong("id");
					result.put(name, id);
				}
				return result;
			}
		);
	}

	private String buildSelectSqlWithInClause(int parameterCount) {
		String placeholders = String.join(DELIMITER, Collections.nCopies(parameterCount, BINDING_PARAMETER));
		return String.format(SELECT_SQL, placeholders);
	}

	private void bindNameParameters(java.sql.PreparedStatement ps, List<String> names) throws java.sql.SQLException {
		for (int i = 0; i < names.size(); i++) {
			ps.setString(i + 1, names.get(i));
		}
	}
}

