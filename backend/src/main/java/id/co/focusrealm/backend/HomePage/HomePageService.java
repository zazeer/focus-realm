package id.co.focusrealm.backend.HomePage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HomePageService {

    @Autowired
    private HomePageRepository homePageRepository;

    public HomePageResponse fetchHomePageDataByUserId(HomePageModel homePageModel){
        HomePageResponse homePageResponse = new HomePageResponse();

        try {

            homePageModel = homePageRepository.fetchHomePageDataByUserId(homePageModel);

            homePageResponse.setHomePageModel(homePageModel);
            homePageResponse.setErrorCode("200");
            homePageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            homePageResponse.setErrorCode("500");
            homePageResponse.setErrorMessage("Error");
            log.error("Error At HomePageService.fetchHomePageDataByUserId");
            throw new RuntimeException(e);
        }

        log.info("Exiting HomePage, fetchHomePageDataByUserId");
        log.info(homePageModel.toString());

        return homePageResponse;
    }

}
