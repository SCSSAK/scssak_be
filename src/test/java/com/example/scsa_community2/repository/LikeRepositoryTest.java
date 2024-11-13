package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Like;
import com.example.scsa_community2.entity.LikeId;
import com.example.scsa_community2.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void testCreateLike() {
        Like like = new Like(1L, "testUser");
        likeRepository.save(like);

        LikeId likeId = new LikeId(1L, "testUser");
        Like foundLike = likeRepository.findById(likeId).orElse(null);
        assertThat(foundLike).isNotNull();
        assertThat(foundLike.getUserId()).isEqualTo("testUser");
    }
}
