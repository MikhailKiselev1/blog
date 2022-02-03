package main.model;

import lombok.Data;
import main.model.enums.MultiuserMode;
import main.model.enums.PostPremoderation;
import main.model.enums.StatisticsIsPublic;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "global_setting")
public class GlobalSetting {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private String value;
}
