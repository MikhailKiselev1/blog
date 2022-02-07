package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@Data
@NoArgsConstructor
public class Tag2Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "post_id", nullable = false, columnDefinition = "INT")
    private int postId;

    @Column(name = "tag_id", nullable = false, columnDefinition = "INT")
    private int tagId;
}
