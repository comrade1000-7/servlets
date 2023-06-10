package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> postsRepository = new ConcurrentHashMap<>();
  private final AtomicLong postId = new AtomicLong(0);
  public List<Post> all() {
    if (!postsRepository.isEmpty()) return new ArrayList<>(postsRepository.values());
    else throw new NullPointerException("Repository is empty");
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(postsRepository.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      postId.getAndIncrement();
      post.setId(postId.get()); //create new post
    } else if (postsRepository.containsKey(post.getId())) {
      postsRepository.put(post.getId(), post); //update id number of the post
    } else throw new NotFoundException("Post with that id doesn't exist");
    return post;
  }

  public void removeById(long id) {
    if (postsRepository.containsKey(id)) {
      postsRepository.remove(id);
    } else {
      throw new NotFoundException("Post with that id doesn't exist");
    }
  }
}
