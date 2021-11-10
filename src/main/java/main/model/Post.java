package main.model;

import lombok.Data;
import main.model.enums.ModerationStatus;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GeneratorType;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    @NotNull
    @Column(name = "moderation_status")
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "moderator_id", columnDefinition = "id")
    private  User moderator;

    @Nullable
    @ManyToMany
    @JoinTable (name = "User2Post", joinColumns = {@JoinColumn (name = "post_id")}, inverseJoinColumns = {@JoinColumn (name = "user_id")})
    private List<User> users;

    @NotNull
    private Date time;

    @NotNull
    private String tittle;

    @NotNull
    private  String text;

    @NotNull
    @Column(name = "view_count")
    private int viewCount;

    @Nullable
    @ManyToMany
    @JoinTable (name = "Post2PostComments", joinColumns = {@JoinColumn (name = "post_id")}, inverseJoinColumns = {@JoinColumn (name = "post_comment_id")})
    private List<PostComments> postComments;
}
