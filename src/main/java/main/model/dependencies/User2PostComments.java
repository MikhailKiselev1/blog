package main.model.dependencies;


import lombok.Data;
import main.model.PostComments;
import main.model.PostVotes;
import main.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User2PostComments")
@Data
public class User2PostComments {
    @EmbeddedId
    private Id id;



    @Embeddable
    @Data
    public static class Id implements Serializable {

        static final long serialVersionUID = 7L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_comment_id", referencedColumnName = "id")
        private PostComments postComments;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;
    }
}
