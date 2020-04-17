package controllers;

import models.Post;
import models.PostRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class PostController extends Controller {

    private final FormFactory formFactory;

    private final PostRepository postRepository;

    private final HttpExecutionContext ec;

    @Inject
    public PostController(FormFactory formFactory, PostRepository postRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.postRepository = postRepository;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }

    public CompletionStage<Result> addPost(final Http.Request request) {
        Post post = formFactory.form(Post.class).bindFromRequest(request).get();
        return postRepository
                .save(post)
                .thenApplyAsync(p -> redirect(routes.PostController.index()), ec.current());
    }

    public CompletionStage<Result> getPosts() {
        return postRepository
                .findAll()
                .thenApplyAsync(postStream -> ok(toJson(postStream.collect(Collectors.toList()))), ec.current());
    }

}
