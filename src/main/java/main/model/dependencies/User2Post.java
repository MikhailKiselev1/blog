package main.model.dependencies;

import lombok.Data;
import main.model.Post;
import main.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User2Post")
@Data
public class User2Post {

    @EmbeddedId
    private Id id;



    @Embeddable
    @Data
    public static class Id implements Serializable {

        static final long serialVersionUID = 3L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", referencedColumnName = "id")
        private Post post;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;
    }
}
