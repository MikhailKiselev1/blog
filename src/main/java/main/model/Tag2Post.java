package main.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tag2post")
public class Tag2Post {

    @EmbeddedId
    private Key key;



    @Data
    @Embeddable
    public static class Key implements Serializable {

        static final long serialVersionUID = 1L;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", columnDefinition = "id")
        private Post post;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "tag_id", columnDefinition = "id")
        private Tags tag;
    }
}