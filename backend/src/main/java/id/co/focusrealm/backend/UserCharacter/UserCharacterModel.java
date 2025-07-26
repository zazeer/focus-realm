package id.co.focusrealm.backend.UserCharacter;

import lombok.Data;

import java.util.Date;

@Data
public class UserCharacterModel {
    private String user_character_id;
    private String user_id;
    private String character_id;
    private Date acquire_date;
    private boolean chosen_character;
}
