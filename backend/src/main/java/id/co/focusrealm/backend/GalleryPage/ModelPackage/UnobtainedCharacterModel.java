package id.co.focusrealm.backend.GalleryPage.ModelPackage;

import lombok.Data;

import java.util.Date;

@Data
public class UnobtainedCharacterModel {

    private String character_id;
    private String character_name;
    private String character_rarity;
    private int price;
    private Date release_date;
    private String file_name;
}
