package com.av.sharebook.logic;

import com.av.sharebook.data.PostRepository;
import com.av.sharebook.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Long id, Post post) {
        post.setId(id);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    public List<Post> findPostsByLocalisation(String localisation) {
        return postRepository.findByLocalisation(localisation);
    }

    public List<Post> findPostsByDate(Date date) {
        return postRepository.findByDate(date);
    }

}