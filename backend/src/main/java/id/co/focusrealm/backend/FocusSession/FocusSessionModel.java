package id.co.focusrealm.backend.FocusSession;

import lombok.Data;

import java.util.Date;

@Data
public class FocusSessionModel {
    private String focus_session_id;
    private String user_id;
    private String analytics_id;
    private int total_focus_duration;
    private int total_break_duration;
    private int interval;
    private int total_coins_made;
    private Date session_date;
}
