package id.co.focusrealm.backend.UserScenery;

import lombok.Data;

import java.util.Date;

@Data
public class UserSceneryModel {
    private String user_scenery_id;
    private String user_id;
    private String scenery_id;
    private Date acquire_date;
    private boolean chosen_scenery;
}
