package com.vladmihalcea.book.hpjp.hibernate.mapping;

import com.vladmihalcea.book.hpjp.util.AbstractTest;
import org.hibernate.annotations.DynamicUpdate;
import org.junit.Test;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @author Vlad Mihalcea
 */
public class DynamicUpdateTest extends AbstractTest {

    @Override
    protected Class<?>[] entities() {
        return new Class<?>[] {
            Post.class
        };
    }

    @Test
    public void test() {

        doInJPA(entityManager -> {
            Post post = new Post();
            post.setId(1L);
            post.setTitle("High-Performance Java Persistence");
            entityManager.persist(post);
        });
        doInJPA(entityManager -> {
            Post post = entityManager.find(Post.class, 1L);
            LOGGER.info("Fetched post: {}", post);
            post.setScore(12);
        });
    }

    @Entity(name = "Post")
    @Table(name = "post")
    @DynamicUpdate
    public static class Post {

        @Id
        private Long id;

        private String title;

        private long score;

        @Column(name = "created_on", nullable = false, updatable = false)
        private Timestamp createdOn;

        @Transient
        private String creationTimestamp;

        public Post() {
            this.createdOn = new Timestamp(System.currentTimeMillis());
        }

        public String getCreationTimestamp() {
            if(creationTimestamp == null) {
                creationTimestamp = DateTimeFormatter.ISO_DATE_TIME.format(
                    createdOn.toLocalDateTime()
                );
            }
            return creationTimestamp;
        }

        @Override
        public String toString() {
            return String.format(
                "Post{\n" +
                "  id=%d\n" +
                "  title='%s'\n" +
                "  score=%d\n" +
                "  creationTimestamp='%s'\n" +
                '}', id, title, score, getCreationTimestamp()
            );
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public Timestamp getCreatedOn() {
            return createdOn;
        }
    }
}
