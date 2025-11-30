package api.service

import api.service.dto.result.PostResults
import domain.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {

    @Transactional(readOnly = true)
    fun getPosts(lastPostId: Long?, limit: Long): PostResults =
        (lastPostId?.let { postRepository.findAllPost(it, limit) }
            ?: postRepository.findAllPost(limit))
            .toList()
            .onEach { it.postTags.size }
            .let { posts ->
                PostResults.of(posts = posts,
                               hasNext = posts.size >= limit,
                               nextPostId = posts.takeIf { it.size >= limit }?.lastOrNull()?.postId) }

    @Transactional(readOnly = true)
    fun getPostsByTag(tagName: String, lastPostId: Long?, limit: Long): PostResults =
        (lastPostId?.let { postRepository.findPostByTag(tagName, it, limit) }
            ?: postRepository.findPostByTag(tagName, limit))
            .toList()
            .onEach { it.postTags.size }
            .let { posts ->
                PostResults.of(posts = posts,
                               hasNext = posts.size >= limit,
                               nextPostId = posts.takeIf { it.size >= limit }?.lastOrNull()?.postId) }

    @Transactional(readOnly = true)
    fun getPostAutoCompleteSuggestions(postName: String, lastPostId: Long?, limit: Long): PostResults =
        (lastPostId?.let { postRepository.findPostByAutoCompleteSuggestions(postName, it, limit) }
            ?: postRepository.findPostByAutoCompleteSuggestions(postName, limit))
            .toList()
            .onEach { it.postTags.size }
            .let { posts ->
                PostResults.of(posts = posts,
                               hasNext = posts.size >= limit,
                               nextPostId = posts.takeIf { it.size >= limit }?.lastOrNull()?.postId) }

}

