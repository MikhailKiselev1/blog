package main.model.dependencies;

import lombok.Data;
import main.model.PostVotes;
import main.model.User;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Users2PostVotes")
@Data
public class Users2PostVotes {

        @EmbeddedId
        private Id id;



        @Embeddable
        @Data
        public static class Id implements Serializable {

            static final long serialVersionUID = 2L;

            @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "post_vote_id", referencedColumnName = "id")
            private PostVotes postVotes;

            @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "user_id", referencedColumnName = "id")
            private User user;
        }

}
