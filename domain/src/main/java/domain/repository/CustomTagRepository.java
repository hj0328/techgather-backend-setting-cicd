package domain.repository;

import domain.entity.Tag;

import java.util.List;

public interface CustomTagRepository {

	void saveAll(List<Tag> tags);
}

