package id.co.focusrealm.backend.TimerPage;

import id.co.focusrealm.backend.Common.BaseResponse;
import id.co.focusrealm.backend.TimerPage.Models.TimerPageModel;
import lombok.Data;

@Data
public class TimerPageResponse extends BaseResponse {
    private TimerPageModel timerPageModel;
}
