package domain.repository;

import domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "ORDER BY p.postId DESC")
	List<Post> findAllPost(@Param("limit") Long limit);

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "WHERE p.postId < :lastPostId " +
		   "ORDER BY p.postId DESC")
	List<Post> findAllPost(@Param("lastPostId") Long lastPostId, @Param("limit") Long limit);

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "WHERE pt.tag.name = :tagName " +
		   "ORDER BY p.postId DESC")
	List<Post> findPostByTag(@Param("tagName") String tagName, @Param("limit") Long limit);

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "WHERE pt.tag.name = :tagName AND p.postId < :lastPostId " +
		   "ORDER BY p.postId DESC")
	List<Post> findPostByTag(@Param("tagName") String tagName, @Param("lastPostId") Long lastPostId, @Param("limit") Long limit);

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "WHERE p.title LIKE CONCAT(:postName, '%') " +
		   "ORDER BY p.postId DESC")
	List<Post> findPostByAutoCompleteSuggestions(@Param("postName") String postName, @Param("limit") Long limit);

	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag " +
		   "WHERE p.title LIKE CONCAT(:postName, '%') AND p.postId < :lastPostId " +
		   "ORDER BY p.postId DESC")
	List<Post> findPostByAutoCompleteSuggestions(@Param("postName") String postName, @Param("lastPostId") Long lastPostId, @Param("limit") Long limit);
}