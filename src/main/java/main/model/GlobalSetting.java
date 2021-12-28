package main.model;

import lombok.Data;
import main.model.enums.GlobalSettingValue;

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

    private GlobalSettingValue globalSettingValue;

    private boolean value;
}
