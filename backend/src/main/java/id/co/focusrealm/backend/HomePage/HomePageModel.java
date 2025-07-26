package id.co.focusrealm.backend.HomePage;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;

@Data
public class HomePageModel {

    // Yang diperlukan dari database User
    private String user_id;
    private String username;
    private int user_coins;

    // Yang diperlukan dari UserScenery
    private String scenery_id;
    private String scenery_name;
    private String scenery_file_name;

    // Yang diperlukan dari UserCharacter
    private String character_id;
    private String character_name;
    private String character_file_name;

}
