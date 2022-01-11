package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "captcha_codes")
public class CaptchaCodes {

    @Id
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
