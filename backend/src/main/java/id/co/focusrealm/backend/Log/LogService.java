package id.co.focusrealm.backend.Log;

import id.co.focusrealm.backend.Common.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public String insertLog(String user_id, String type, Date created_at){
        LogModel logModel = new LogModel();

        try {

            logModel.setUser_id(user_id);
            logModel.setType(type);
            logModel.setCreated_at(created_at);
            logModel.setLog_id(generateLogId());

            logRepository.insertLog(logModel);

        } catch (Exception e) {
            log.info("Error at LogService insertLog", e);
            throw new RuntimeException(e);
        }

        return logModel.getLog_id();

    }

    public String generateLogId(){
        String newUserCharacterId = null;

        try {

            if(logRepository.checkHasValue() == false){
                newUserCharacterId = "L001";
            } else {
                String lastLogId = logRepository.getLastLogId();
                newUserCharacterId = Utility.generateNextId(lastLogId);
            }

        } catch (Exception e) {
            log.error("Error at logService generateLogId", e);
            throw new RuntimeException(e);
        }

        return newUserCharacterId;
    }

}

