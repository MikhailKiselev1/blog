package main.model.dependencies;

import lombok.Data;
import main.model.Post;
import main.model.PostVotes;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Post2PostVotes")
@Data
public class Post2PostVotes {

    @EmbeddedId
    private Id id;



    @Embeddable
    @Data
    public static class Id implements Serializable {

        static final long serialVersionUID = 5L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_vote_id", referencedColumnName = "id")
        private PostVotes postVotes;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", referencedColumnName = "id")
        private Post post;
    }
}
