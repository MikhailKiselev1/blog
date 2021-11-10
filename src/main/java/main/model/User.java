package main.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    @Column(name = "is_moderator")
    private boolean isModerator;

    @NotNull
    @Column(name = "reg_time")
    private Date regTime;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Nullable
    private String code;

    @Nullable
    private String photo;

    @Nullable
    @ManyToMany
    @JoinTable (name = "User2Post", joinColumns = {@JoinColumn (name = "user_id")}, inverseJoinColumns = {@JoinColumn (name = "post_id")})
    private List<Post> posts;

    @Nullable
    @ManyToMany
    @JoinTable (name = "User2PostVotes", joinColumns = {@JoinColumn (name = "user_id")}, inverseJoinColumns = {@JoinColumn (name = "post_vote_id")})
    private List<PostVotes> postsVotes;

    @Nullable
    @ManyToMany
    @JoinTable (name = "User2PostComments", joinColumns = {@JoinColumn (name = "user_id")}, inverseJoinColumns = {@JoinColumn (name = "post_comment_id")})
    private List<PostComments> postComments;


    @OneToMany
    @JoinColumn(name = "moderator_id")
    private List<Post> postForModerator;

}
