package id.co.focusrealm.backend.User;

import id.co.focusrealm.backend.Analytics.AnalyticsModel;
import id.co.focusrealm.backend.Analytics.AnalyticsService;
import id.co.focusrealm.backend.Common.Utility;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterRepository;
import id.co.focusrealm.backend.UserCharacter.UserCharacterService;
import id.co.focusrealm.backend.UserScenery.UserSceneryModel;
import id.co.focusrealm.backend.UserScenery.UserSceneryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCharacterService  userCharacterService;

    @Autowired
    private UserSceneryService userSceneryService;

    @Autowired
    private AnalyticsService  analyticsService;

    private String defaultMusic = "M001";
    private String defaultAmbient = "A001";
    private String defaultCharacter = "CH001";
    private String defaultScenery = "SC001";
    private int defaultCoins = 0;
    private int defaultPity = 100;
    @Autowired
    private UserCharacterRepository userCharacterRepository;

    public UserResponse insertUser(UserModel user){
        UserResponse userResponse = new UserResponse();

        try {

            if(userRepository.checkUserNameExists(user.getUsername()) == true){
                userResponse.setErrorCode("204");
                userResponse.setErrorMessage("Username already exists");
            } else if (userRepository.checkEmailTaken(user.getEmail()) == true){
                userResponse.setErrorCode("204");
                userResponse.setErrorMessage("Email already exists");
            } else {

                // Setting User Data
                user.setUser_Id(generateUserId());
                user.setMusic_Id(defaultMusic);
                user.setAmbient_Id(defaultAmbient);

                Date created_at = (new Timestamp(System.currentTimeMillis()));

                user.setCreated_at(created_at);
                user.setPity(defaultPity);
                user.setCoins(defaultCoins);

                userRepository.insertUser(user);

                // Setting User Character Data
                UserCharacterModel userCharacterModel = new UserCharacterModel();
                userCharacterModel.setUser_id(user.getUser_Id());
                userCharacterModel.setCharacter_id(defaultCharacter);
                userCharacterModel.setAcquire_date(created_at);
                userCharacterModel.setChosen_character(true);
                userCharacterService.insertUserCharacter(userCharacterModel);

                // Setting User Scenery Data
                UserSceneryModel userSceneryModel = new UserSceneryModel();
                userSceneryModel.setUser_id(user.getUser_Id());
                userSceneryModel.setScenery_id(defaultScenery);
                userSceneryModel.setAcquire_date(created_at);
                userSceneryModel.setChosen_scenery(true);
                userSceneryService.insertUserScenery(userSceneryModel);

                // Setting User Analytics Data
                AnalyticsModel analyticsModel = new AnalyticsModel();
                analyticsModel.setUser_id(user.getUser_Id());
                analyticsModel.setTotal_focus_duration(0);
                analyticsModel.setTotal_focus_session(0);
                analyticsModel.setTotal_coins_made(0);
                analyticsService.insertAnalytics(analyticsModel);

                userResponse.setUser(user);
                userResponse.setErrorCode("200");
                userResponse.setErrorMessage("Success");
            }

        } catch (Exception e) {
            log.error("Error at UserService.insertUser");
            userResponse.setErrorCode("500");
            userResponse.setErrorMessage("Error");

            throw new RuntimeException(e);
        }

        return userResponse;
    }

    public String generateUserId(){
        String newUserId = null;

        try {

            if(userRepository.checkHasValue() == false){
                newUserId = "U001";
            } else {
                String lastUserId = userRepository.getLastUserId();
                newUserId = Utility.generateNextId(lastUserId);
            }

        } catch (Exception e) {
            log.error("Error at UserService.generateUserId");
            throw new RuntimeException(e);
        }

        return newUserId;
    }

    public UserResponse fetchUser(UserModel user){
          UserResponse userResponse = new UserResponse();
          try {

              int getFetchUserCode = userRepository.fetchUser(user);

              if(getFetchUserCode == 1){

                  userResponse.setErrorCode("204");
                  userResponse.setErrorMessage("User Not Found");

              } else if (getFetchUserCode == 2){

                  userResponse.setErrorCode("204");
                  userResponse.setErrorMessage("Wrong Password");

              } else if  (getFetchUserCode == 3){

                  userResponse.setUser(user);
                  userResponse.setErrorCode("200");
                  userResponse.setErrorMessage("Success");

              }

          } catch (Exception e) {
              userResponse.setErrorCode("500");
              userResponse.setErrorMessage("Error");
              log.error("Error at UserService.fetchUser");
              throw new RuntimeException(e);
          }

          return userResponse;
    }

}
