package main.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "users")
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
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Post> posts;

    @OneToMany
    @JoinColumn(name = "moderator_id")
    private List<Post> postForModerator;

}
