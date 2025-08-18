package id.co.focusrealm.backend.GachaPage;

import lombok.Data;

@Data
public class GachaModel {

    private String user_id;
    private int user_coins;
    private int user_pity;

    private int gacha_amount;

}
