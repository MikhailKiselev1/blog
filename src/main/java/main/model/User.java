package main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    private boolean isModerator;

    @NotNull
    private Date regTime;

    @NotNull
    private String name;

    @NotNull
    private String eMail;

    @NotNull
    private String password;

    private String code;

    private String foto;
}
