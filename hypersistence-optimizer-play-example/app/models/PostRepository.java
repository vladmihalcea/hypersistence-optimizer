package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(PostRepositoryImpl.class)
public interface PostRepository {

    CompletionStage<Post> save(Post person);

    CompletionStage<Stream<Post>> findAll();
}
