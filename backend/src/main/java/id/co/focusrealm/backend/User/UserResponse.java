package id.co.focusrealm.backend.User;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;

@Data
public class UserResponse extends BaseResponse {
    private UserModel user;
}
