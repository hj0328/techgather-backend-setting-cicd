package domain.repository;

import domain.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	List<PostTag> findAllByPostPostIdAndTagIdIn(Long postId, java.util.Collection<Long> tagIds);

	List<PostTag> findAllByPostPostIdInAndTagIdIn(java.util.Collection<Long> postIds, java.util.Collection<Long> tagIds);
}

