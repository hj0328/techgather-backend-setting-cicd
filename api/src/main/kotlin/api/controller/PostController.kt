package api.controller

import api.controller.dto.response.PostResponseList
import api.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    fun getPosts(
        @RequestParam(required = false) lastPostId: Long?,
        @RequestParam(defaultValue = "10") limit: Long
    ): PostResponseList {
        val results = postService.getPosts(lastPostId, limit)
        return PostResponseList.from(results)
    }

    @GetMapping("/search")
    @ResponseStatus(code = HttpStatus.OK)
    fun getPostsByTag(
        @RequestParam tagName: String,
        @RequestParam(required = false) lastPostId: Long?,
        @RequestParam(defaultValue = "10") limit: Long
    ): PostResponseList {
        val results = postService.getPostsByTag(tagName, lastPostId, limit)
        return PostResponseList.from(results)
    }

    @GetMapping("/suggestions")
    @ResponseStatus(code = HttpStatus.OK)
    fun getPostAutoCompleteSuggestions(
        @RequestParam postName: String,
        @RequestParam(required = false) lastPostId: Long?,
        @RequestParam(defaultValue = "10") limit: Long
    ): PostResponseList {
        if (postName.isBlank() || postName.length < 2) {
            return PostResponseList.EMPTY
        }
        val results = postService.getPostAutoCompleteSuggestions(postName, lastPostId, limit)
        return PostResponseList.from(results)
    }

}

