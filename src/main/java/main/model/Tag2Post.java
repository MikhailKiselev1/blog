package main.model;

import lombok.Data;
import main.model.Post;
import main.model.Tags;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@Table(name = "Post2PostVotes")
public class Tag2Post {

    @EmbeddedId
    private Key key;

    @NotNull
    @Column(name = "post_id")
    private String postId;


    @NotNull
    @Column(name = "tag_id")
    private int tagId;

    @Data
    @Embeddable
    public static class Key implements Serializable {

        static final long serialVersionUID = 1L;

        @NotNull
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", columnDefinition = "id")
        private Post post;


        @NotNull
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "tag_id", columnDefinition = "id")
        private Tags tag;
    }
}