package domain.repository;

import domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query(value = "SELECT * FROM post p " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findAllPost(@Param("limit") Long limit);

	@Query(value = "SELECT * FROM post p " +
				   "WHERE p.post_id < :lastPostId " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findAllPost(@Param("lastPostId") Long lastPostId, @Param("limit") Long limit);

	@Query(value = "SELECT DISTINCT p.* FROM post p " +
				   "INNER JOIN post_tag pt ON p.post_id = pt.post_id " +
				   "INNER JOIN tag t ON pt.tag_id = t.id " +
				   "WHERE t.name = :tagName " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findPostByTag(@Param("tagName") String tagName, @Param("limit") Long limit);

	@Query(value = "SELECT DISTINCT p.* FROM post p " +
				   "INNER JOIN post_tag pt ON p.post_id = pt.post_id " +
				   "INNER JOIN tag t ON pt.tag_id = t.id " +
				   "WHERE t.name = :tagName AND p.post_id < :lastPostId " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findPostByTag(@Param("tagName") String tagName, @Param("lastPostId") Long lastPostId, @Param("limit") Long limit);

	@Query(value = "SELECT * FROM post p " +
				   "WHERE p.title LIKE CONCAT(:postName, '%') " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findPostByAutoCompleteSuggestions(@Param("postName") String postName, @Param("limit") Long limit);

	@Query(value = "SELECT * FROM post p " +
				   "WHERE p.title LIKE CONCAT(:postName, '%') AND p.post_id < :lastPostId " +
				   "ORDER BY p.post_id DESC " +
				   "LIMIT :limit", nativeQuery = true)
	List<Post> findPostByAutoCompleteSuggestions(@Param("postName") String postName, @Param("lastPostId") Long lastPostId, @Param("limit") Long limit);
}