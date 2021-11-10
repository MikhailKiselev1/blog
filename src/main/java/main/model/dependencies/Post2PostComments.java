package main.model.dependencies;

import lombok.Data;
import main.model.Post;
import main.model.PostComments;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Post2PostComments")
@Data
public class Post2PostComments {

    @EmbeddedId
    private Id id;
    
    @Embeddable
    @Data
    public static class Id implements Serializable {

        static final long serialVersionUID = 6L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_comment_id", referencedColumnName = "id")
        private PostComments postComments;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", referencedColumnName = "id")
        private Post post;
    }
}
