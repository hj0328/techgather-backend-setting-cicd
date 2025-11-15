package domain.repository;

import domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.url FROM Post p WHERE p.url IN :urls")
    List<String> findUrlsByUrlIn(@Param("urls") List<String> urls);
}