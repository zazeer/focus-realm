package id.co.focusrealm.backend.TimerPage;

import id.co.focusrealm.backend.TimerPage.Models.TimerPageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TimerPageService {

    @Autowired
    private TimerPageRepository timerPageRepository;

    public TimerPageResponse fetchTimerPageDataByUserId(TimerPageModel timerPageModel){
        TimerPageResponse timerPageResponse = new TimerPageResponse();

        try {

            timerPageModel = timerPageRepository.fetchTimerPageDataByUserId(timerPageModel);
            timerPageModel.setAllMusicList(timerPageRepository.fetchAllMusic(timerPageModel));
            timerPageModel.setAllAmbientList(timerPageRepository.fetchAllAmbient(timerPageModel));

            timerPageResponse.setTimerPageModel(timerPageModel);
            timerPageResponse.setErrorCode("200");
            timerPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            timerPageResponse.setErrorCode("400");
            timerPageResponse.setErrorMessage("Error");
            log.error("Error in TimerPageService FetchTimerPageDataByUserId",e);
            throw new RuntimeException(e);
        }

        return timerPageResponse;
    }

}
