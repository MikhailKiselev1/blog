package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class PostVotes {

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private Date time;

    @NotNull
    private byte value;

    @ManyToMany
    @JoinTable (name = "User2PostVotes", joinColumns = {@JoinColumn (name = "post_vote_id")}, inverseJoinColumns = {@JoinColumn (name = "user_id")})
    private List<User> users;

    @ManyToMany
    @JoinTable (name = "Post2PostVotes", joinColumns = {@JoinColumn (name = "post_vote_id")}, inverseJoinColumns = {@JoinColumn (name = "post_id")})
    private List<Post> posts;
}
