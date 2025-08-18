package id.co.focusrealm.backend.Log;

import lombok.Data;

import java.util.Date;

@Data
public class LogModel {

    private String log_id;
    private String user_id;
    private String type;
    private Date created_at;

}
