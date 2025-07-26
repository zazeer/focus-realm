package id.co.focusrealm.backend.FocusSession;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;

@Data
public class FocusSessionResponse extends BaseResponse {
    private FocusSessionModel focusSessionModel;
}
