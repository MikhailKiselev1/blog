package main.api.request;

import lombok.Data;

import java.io.File;

@Data
public class MyProfileRequest {

    private String name;
    private String email;
    private String password;
    private int removePhoto;
}
