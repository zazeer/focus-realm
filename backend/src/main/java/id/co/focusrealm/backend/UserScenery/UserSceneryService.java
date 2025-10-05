package id.co.focusrealm.backend.UserScenery;

import id.co.focusrealm.backend.Common.Utility;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSceneryService {

    @Autowired
    private UserSceneryRepository userSceneryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String insertUserScenery(UserSceneryModel userSceneryModel){

        try {

            userSceneryModel.setUser_scenery_id(generateUserSceneryId());
            userSceneryRepository.insertUserScenery(userSceneryModel);

        } catch (Exception e) {
            log.error("Error at UserSceneryService insertUserScenery", e);
            throw new RuntimeException(e);
        }

        return userSceneryModel.getUser_scenery_id();

    }

    public String generateUserSceneryId(){
        String newUserSceneryId = null;

        try {

            if(userSceneryRepository.checkHasValue() == false){
                newUserSceneryId = "US001";
            } else {
                String lastUserId = userSceneryRepository.getLastUserSceneryId();
                newUserSceneryId = Utility.generateNextId(lastUserId);
            }

        } catch (Exception e) {
            log.error("Error at UserSceneryService generateUserSceneryId", e);
            throw new RuntimeException(e);
        }

        return newUserSceneryId;
    }

}
