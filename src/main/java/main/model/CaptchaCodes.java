package main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class CaptchaCodes {

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private Date time;

    @NotNull
    private short code;

    @NotNull
    @Column(name = "secret_code")
    private short secretCode;
}
