package id.co.focusrealm.backend.ProfilePage;

import id.co.focusrealm.backend.Analytics.AnalyticsRepository;
import id.co.focusrealm.backend.FocusSession.FocusSessionRepository;
import id.co.focusrealm.backend.User.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfilePageService {

    @Autowired
    private ProfilePageRepository profilePageRepository;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Autowired
    private UserRepository userRepository;

    public ProfilePageResponse fetchProfilePageData(ProfilePageModel profilePageModel){

        ProfilePageResponse profilePageResponse = new ProfilePageResponse();

        try {
            ProfilePageModel temporary = profilePageRepository.fetchUserData(profilePageModel);
            profilePageModel.setUsername(temporary.getUsername());
            profilePageModel.setEmail(temporary.getEmail());
            profilePageModel.setProfile_picture_file_name(temporary.getProfile_picture_file_name());

            profilePageModel.setAllTimeUserStatistics(analyticsRepository.fetchAnalyticsDataByUserId(profilePageModel.getUser_id()));
            profilePageModel.setAllUserFocusSessionList(focusSessionRepository.fetchAllFocusSession(profilePageModel.getUser_id(), profilePageModel.getAllTimeUserStatistics().getAnalytics_id()));

            profilePageResponse.setErrorCode("200");
            profilePageResponse.setErrorMessage("SUCCESS");

            profilePageResponse.setProfilePageModel(profilePageModel);
            return profilePageResponse;

        } catch (Exception e) {
            profilePageResponse.setErrorCode("400");
            profilePageResponse.setErrorMessage("ERROR");
            log.error("ERROR AT ProfilePageService, fetchProfilePageData");
            throw new RuntimeException(e);
        }
    }

    public ProfilePageResponse changeUserName(ProfilePageModel profilePageModel){

        ProfilePageResponse profilePageResponse = new ProfilePageResponse();

        try {

            boolean checkUserNameTaken = userRepository.checkUserNameExists(profilePageModel.getUsername());
            if(checkUserNameTaken == true) {
                profilePageResponse.setProfilePageModel(profilePageModel);
                profilePageResponse.setErrorCode("400");
                profilePageResponse.setErrorMessage("USERNAME TAKEN");
                return profilePageResponse;
            }

            profilePageRepository.updateUserName(profilePageModel);
            fetchProfilePageData(profilePageModel);

            profilePageResponse.setErrorCode("200");
            profilePageResponse.setErrorMessage("SUCCESS");
            profilePageResponse.setProfilePageModel(profilePageModel);

            return profilePageResponse;

        } catch (Exception e) {
            profilePageResponse.setErrorCode("400");
            profilePageResponse.setErrorMessage("ERROR AT CHANGING USERNAME");
            log.error("ERROR AT ProfilePageService, changeUserName");
            throw new RuntimeException(e);
        }
    }

    public ProfilePageResponse changePassword(ProfilePageModel profilePageModel){

        ProfilePageResponse profilePageResponse = new ProfilePageResponse();

        try {

            profilePageRepository.updateUserPassword(profilePageModel);
            fetchProfilePageData(profilePageModel);

            profilePageResponse.setErrorCode("200");
            profilePageResponse.setErrorMessage("SUCCESS");
            profilePageResponse.setProfilePageModel(profilePageModel);

            return profilePageResponse;

        } catch (Exception e) {
            profilePageResponse.setErrorCode("400");
            profilePageResponse.setErrorMessage("ERROR AT CHANGING PASSWORD");
            log.error("ERROR AT ProfilePageService, changeUserName");
            throw new RuntimeException(e);
        }
    }

    public ProfilePageResponse changeEmail(ProfilePageModel profilePageModel){

        ProfilePageResponse profilePageResponse = new ProfilePageResponse();

        try {

            boolean checkEmailTaken = userRepository.checkEmailTaken(profilePageModel.getEmail());
            if(checkEmailTaken == true){
                profilePageResponse.setProfilePageModel(profilePageModel);
                profilePageResponse.setErrorCode("400");
                profilePageResponse.setErrorMessage("EMAIL TAKEN");
                return profilePageResponse;
            }

            profilePageRepository.updateEmail(profilePageModel);
            fetchProfilePageData(profilePageModel);

            profilePageResponse.setErrorCode("200");
            profilePageResponse.setErrorMessage("SUCCESS");
            profilePageResponse.setProfilePageModel(profilePageModel);

            return profilePageResponse;

        } catch (Exception e) {
            profilePageResponse.setErrorCode("400");
            profilePageResponse.setErrorMessage("ERROR AT CHANGING PASSWORD");
            log.error("ERROR AT ProfilePageService, changeUserName");
            throw new RuntimeException(e);
        }
    }

}
