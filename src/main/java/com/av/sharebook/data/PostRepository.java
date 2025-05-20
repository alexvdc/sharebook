package com.av.sharebook.data;

import com.av.sharebook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByLocalisation(String localisation);

    List<Post> findByDate(Date date);
}