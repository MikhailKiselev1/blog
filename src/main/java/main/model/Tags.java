package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Tags {

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(name = "Tag2Post", joinColumns = {@JoinColumn (name = "tag_id")}, inverseJoinColumns = {@JoinColumn (name = "post_id")})
    private List<Post> posts;
}
