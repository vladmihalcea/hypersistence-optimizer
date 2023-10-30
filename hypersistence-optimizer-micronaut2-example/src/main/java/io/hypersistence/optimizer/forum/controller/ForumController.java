package io.hypersistence.optimizer.forum.controller;

import io.hypersistence.optimizer.forum.domain.Post;
import io.hypersistence.optimizer.forum.repository.PostRepository;
import io.hypersistence.optimizer.forum.service.ForumService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import javax.inject.Inject;

@Controller("/api")
public class ForumController {

    @Inject
    PostRepository postRepository;

    @Inject
    ForumService forumService;

    @Get("/organization")
    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }
}
