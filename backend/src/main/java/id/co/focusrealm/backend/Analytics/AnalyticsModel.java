package id.co.focusrealm.backend.Analytics;

import lombok.Data;

@Data
public class AnalyticsModel {

    private String analytics_id;
    private String user_id;
    private int daily_focus_duration;
    private int daily_total_session;
    private int daily_coins_made;
    private int longest_daily_streak;

}
