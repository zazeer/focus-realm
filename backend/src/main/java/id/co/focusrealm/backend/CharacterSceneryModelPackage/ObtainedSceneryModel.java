package id.co.focusrealm.backend.CharacterSceneryModelPackage;

import lombok.Data;

import java.util.Date;

@Data
public class ObtainedSceneryModel {

    private String scenery_id;
    private String scenery_name;
    private String scenery_rarity;
    private String scenery_description;
    private int price;
    private Date release_date;
    private String file_name;

    private Date acquire_date;
    private boolean chosen_scenery;

}
