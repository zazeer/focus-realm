package id.co.focusrealm.backend.Common;

import lombok.Data;

@Data
public class BaseResponse {
    private String errorCode;
    private String errorMessage;
}
