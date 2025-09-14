package id.co.focusrealm.backend.Analytics;

import lombok.Data;

@Data
public class AnalyticsModel {

    private String analytics_id;
    private String user_id;
    private int total_focus_duration;
    private int total_focus_session;
    private int total_coins_made;
}
