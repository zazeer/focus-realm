package id.co.focusrealm.backend.FocusSession;

import id.co.focusrealm.backend.Analytics.AnalyticsModel;
import id.co.focusrealm.backend.Analytics.AnalyticsRepository;
import id.co.focusrealm.backend.Common.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Slf4j
public class FocusSessionService {

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public FocusSessionResponse insertFocusSession(FocusSessionModel focusSessionModel){

        FocusSessionResponse focusSessionResponse = new FocusSessionResponse();

        try {
            focusSessionModel.setFocus_session_id(generateFocusSessionId());
            focusSessionModel.setAnalytics_id(analyticsRepository.getAnalyticsIdByUserId(focusSessionModel.getUser_id()));
            focusSessionModel.setTotal_coins_made(calculateCoinsMade(focusSessionModel));

            Date created_at = (new Timestamp(System.currentTimeMillis()));
            focusSessionModel.setSession_date(created_at);

            focusSessionRepository.insertFocusSession(focusSessionModel);
            focusSessionRepository.updateUserCoins(focusSessionModel);

            AnalyticsModel tempAnalyticsModel = new AnalyticsModel();
            tempAnalyticsModel.setUser_id(focusSessionModel.getUser_id());
            tempAnalyticsModel.setTotal_focus_duration(focusSessionModel.getTotal_focus_duration());
            tempAnalyticsModel.setTotal_coins_made(focusSessionModel.getTotal_coins_made());
            analyticsRepository.updateAnalytics(tempAnalyticsModel);

            focusSessionResponse.setFocusSessionModel(focusSessionModel);
            focusSessionResponse.setErrorCode("200");
            focusSessionResponse.setErrorMessage("Success");

        } catch (Exception e) {
            focusSessionResponse.setErrorCode("400");
            focusSessionResponse.setErrorMessage("Failed");
            log.error("Error at focusSessionService insertFocusSession", e);
            throw new RuntimeException(e);
        }

        return focusSessionResponse;
    }

    public int calculateCoinsMade(FocusSessionModel focusSessionModel){
        int coinsMade = 0;

        try {

            coinsMade = (focusSessionModel.getTotal_focus_duration() - focusSessionModel.getTotal_break_duration());

        } catch (Exception e) {
            log.error("Error at focusSessionService calculateCoinsMade", e);
            throw new RuntimeException(e);
        }

        return coinsMade;
    }

    public String generateFocusSessionId(){
        String newAnalyticsId = null;

        try {

            if(focusSessionRepository.checkHasValue() == false){
                newAnalyticsId = "FS001";
            } else {
                String lastUserId = focusSessionRepository.getLastFocusSessionId();
                newAnalyticsId = Utility.generateNextId(lastUserId);
            }

        } catch (Exception e) {
            log.error("Error at FocusSessionService generateFocusSessionId");
            throw new RuntimeException(e);
        }

        return newAnalyticsId;
    }

}
