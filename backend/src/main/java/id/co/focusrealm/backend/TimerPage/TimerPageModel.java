package id.co.focusrealm.backend.TimerPage;

import lombok.Data;

@Data
public class TimerPageModel {

    // Yang diperlukan dari database User
    private String user_id;

    // Yang diperlukan dari UserScenery
    private String scenery_id;
    private String scenery_name;
    private String scenery_file_name;

    // Yang diperlukan dari UserCharacter
    private String character_id;
    private String character_name;
    private String character_file_name;

    // Yang diperlukan dari Music
    private String music_id;
    private String music_title;
    private String music_file_name;
    private int music_duration;

    // Yang diperlukan dari Ambient
    private String ambient_id;
    private String ambient_title;
    private String ambient_file_name;


}
