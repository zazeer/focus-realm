package id.co.focusrealm.backend.CharacterSceneryModel;

import lombok.Data;

import java.util.Date;

@Data
public class CharacterModel {

    private String character_id;
    private String character_name;
    private String character_rarity;
    private String character_description;
    private int price;
    private Date release_date;
    private String file_name;

}
