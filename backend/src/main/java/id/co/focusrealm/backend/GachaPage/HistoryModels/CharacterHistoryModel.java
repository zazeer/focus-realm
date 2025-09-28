package id.co.focusrealm.backend.GachaPage.HistoryModels;

import lombok.Data;

import java.util.Date;

@Data
public class CharacterHistoryModel {

    private String character_id;
    private String character_name;
    private String character_rarity;
    private Date obtainedDate;

}
