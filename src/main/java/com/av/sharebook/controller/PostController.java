package com.av.sharebook.controller;

import com.av.sharebook.config.SecurityUtil;
import com.av.sharebook.logic.PostService;
import com.av.sharebook.logic.UserService;
import com.av.sharebook.model.Post;
import com.av.sharebook.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam
    Long userId) {
    Optional<User> userOpt = userService.getUserById(userId);
    if (userOpt.isEmpty()) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    post.setUser(userOpt.get());
    Post createdPost = postService.createPost(post);
    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Optional<Post> existingPostOpt = postService.getPostById(id);
        if (existingPostOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Post existingPost = existingPostOpt.get();
        String currentUsername = SecurityUtil.getCurrentUsername();
        boolean isAdmin = existingPost.getUser().getUserRoles().contains("ADMIN");

        if (!existingPost.getUser().getUsername().equals(currentUsername) && !isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Post updatedPost = postService.updatePost(id, post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    //     Optional<Post> existingPostOpt = postService.getPostById(id);
    //     if (existingPostOpt.isEmpty()) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }

    //     Post existingPost = existingPostOpt.get();
    //     String currentUsername = SecurityUtil.getCurrentUsername();
    //     boolean isAdmin = existingPost.getUser().getUserRoles().contains("ADMIN");

    //     if (!existingPost.getUser().getUsername().equals(currentUsername) && !isAdmin) {
    //         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //     }

    //     postService.deletePost(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

//     @DeleteMapping("/{id}")
// public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//     Optional<Post> existingPostOpt = postService.getPostById(id);
//     if (existingPostOpt.isEmpty()) {
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }

//     Post existingPost = existingPostOpt.get();
//     boolean isAdmin = existingPost.getUser().getUserRoles().contains("ADMIN");

//     if (!isAdmin) { // Only check if the user is NOT an ADMIN
//         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//     }

//     postService.deletePost(id);
//     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//     Optional<Post> existingPostOpt = postService.getPostById(id);
//     if (existingPostOpt.isEmpty()) {
//         System.out.println("Post not found with ID: " + id);
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }

//     Post existingPost = existingPostOpt.get();
//     String currentUsername = SecurityUtil.getCurrentUsername();
//     boolean isAdmin = existingPost.getUser().getUserRoles().contains("ADMIN");
//     System.out.println("RO");

//     System.out.println("Attempting to delete post ID: " + id);
//     System.out.println("Current user: " + currentUsername);
//     System.out.println("Post owner: " + existingPost.getUser().getUsername());
//     System.out.println("Is current user ADMIN? " + isAdmin);

//     if (!existingPost.getUser().getUsername().equals(currentUsername) && !isAdmin) {
//         System.out.println("Deletion forbidden.");
//         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//     }

//     System.out.println("Post deleted successfully (or deletion proceeding).");
//     postService.deletePost(id);
//     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// }

@DeleteMapping("/{id}")
public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    Optional<Post> existingPostOpt = postService.getPostById(id);
    if (existingPostOpt.isEmpty()) {
        System.out.println("Post not found with ID: " + id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Post existingPost = existingPostOpt.get();
    String currentUsername = SecurityUtil.getCurrentUsername();
    boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    System.out.println("Attempting to delete post ID: " + id);
    System.out.println("Current user: " + currentUsername);
    System.out.println("Post owner: " + existingPost.getUser().getUsername());
    System.out.println("Is current user ADMIN? " + isAdmin);

    if (!existingPost.getUser().getUsername().equals(currentUsername) && !isAdmin) {
        System.out.println("Deletion forbidden.");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    System.out.println("Post deleted successfully (or deletion proceeding).");
    postService.deletePost(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

    @GetMapping("/localisation")
    public ResponseEntity<List<Post>> findPostsByLocalisation(@RequestParam String localisation) {
        if (localisation == null || localisation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Post> posts = postService.findPostsByLocalisation(localisation);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Post>> findPostsByDate(@RequestParam Date date) {
        if (date == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Post> posts = postService.findPostsByDate(date);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}