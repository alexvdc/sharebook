package com.av.sharebook;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import com.av.sharebook.logic.PostService;
// import com.av.sharebook.model.Post;

@SpringBootApplication
public class SharebookApplication {

	// @Autowired 
	// private PostService postService;

	public static void main(String[] args) {
		SpringApplication.run(SharebookApplication.class, args);
	}

	// @Override
	// public void run(String... args) throws Exception {
		
	// 	// postService.getAllPosts();
	// 	List<Post> posts = postService.getAllPosts();

	// 	for (Post post : posts) {
	// 		System.out.println(post);
	// 	}

		
	// }
}
