package id.co.focusrealm.backend.GachaPage.HistoryModels;

import lombok.Data;

import java.util.Date;

@Data
public class SceneryHistoryModel {

    private String scenery_id;
    private String scenery_name;
    private String scenery_rarity;
    private Date obtainedDate;

}
