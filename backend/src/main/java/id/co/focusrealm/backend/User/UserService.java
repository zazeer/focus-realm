package id.co.focusrealm.backend.User;

import id.co.focusrealm.backend.Common.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private String defaultMusic = "M001";
    private String defaultAmbient = "A001";

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
                user.setUser_Id(generateUserId());
                user.setMusic_Id(defaultMusic);
                user.setAmbient_Id(defaultAmbient);

                user.setCreated_at(new Timestamp(System.currentTimeMillis()));
                user.setPity(0);
                user.setCoins(0);

                userRepository.insertUser(user);

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
              log.error("Error at UserService.fetchUser");
              throw new RuntimeException(e);
          }

          return userResponse;
    }

}
