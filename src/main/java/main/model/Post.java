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
@Table(name = "posts")
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
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @Nullable
    @ManyToOne
    private User user;

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
    @OneToMany
    private List<PostComments> postComments;

}
