package id.co.focusrealm.backend.GalleryPage.ModelPackage;

import lombok.Data;

import java.util.Date;

@Data
public class ObtainedCharacterModel {

    private String character_id;
    private String character_name;
    private String character_rarity;
    private int price;
    private Date release_date;
    private String file_name;

    private Date acquire_date;
    private boolean chosen_character;

}
