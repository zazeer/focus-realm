package id.co.focusrealm.backend.User;

import lombok.Data;

import java.util.Date;

@Data
public class UserModel {

    private String user_Id;
    private String music_Id;
    private String ambient_Id;
    private String timer_Id;
    private String username;
    private String email;
    private String password;
    private int coins;
    private int pity;
    private Date created_at;
}
