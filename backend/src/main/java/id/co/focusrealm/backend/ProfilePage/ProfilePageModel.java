package id.co.focusrealm.backend.ProfilePage;

import id.co.focusrealm.backend.Analytics.AnalyticsModel;
import id.co.focusrealm.backend.FocusSession.FocusSessionModel;
import lombok.Data;

import java.util.List;

@Data
public class ProfilePageModel {

    private String user_id;
    private String username;
    private String email;
    private String password;

    private String profile_picture_file_name;

    private AnalyticsModel allTimeUserStatistics;
    private List<FocusSessionModel> allUserFocusSessionList;
}
