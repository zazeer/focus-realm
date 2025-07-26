package id.co.focusrealm.backend.UserCharacter;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;

@Data
public class UserCharacterResponse extends BaseResponse {
    UserCharacterModel userCharacterModel;
}
