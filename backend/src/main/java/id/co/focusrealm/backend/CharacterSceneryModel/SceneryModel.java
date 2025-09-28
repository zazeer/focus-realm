package id.co.focusrealm.backend.CharacterSceneryModel;

import lombok.Data;

import java.util.Date;

@Data
public class SceneryModel {

    private String scenery_id;
    private String scenery_name;
    private String scenery_rarity;
    private int price;
    private String file_name;
    private Date release_date;
    private String scenery_description;

}
