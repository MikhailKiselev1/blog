package main.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class PostComments {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", columnDefinition = "id")
    private PostComments parentPostComments;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Date time;

    @NotNull
    private String text;
}
