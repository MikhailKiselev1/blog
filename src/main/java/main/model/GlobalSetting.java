package main.model;

import lombok.Data;
import main.model.enums.SettingName;
import main.model.enums.SettingValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class GlobalSetting {

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String code;

    @NotNull
    private SettingName name;

    @NotNull
    private SettingValue value;
}
