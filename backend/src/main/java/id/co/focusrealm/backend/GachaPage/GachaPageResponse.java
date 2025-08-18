package id.co.focusrealm.backend.GachaPage;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class GachaPageResponse extends BaseResponse {

    private GachaModel gachaModel;

}
