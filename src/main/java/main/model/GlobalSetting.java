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

    @Column(name = "MULTIUSER_MODE")
    private MultiuserMode mode;

    @Column(name = "POST_PREMODERATION")
    private PostPremoderation postPremoderation;

    @Column(name = "STATISTICS_IS_PUBLIC")
    private StatisticsIsPublic statisticsIsPublic;
}
