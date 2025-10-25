package id.co.focusrealm.backend.UserCharacter;

import id.co.focusrealm.backend.Common.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCharacterService {

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String insertUserCharacter(UserCharacterModel userCharacterModel){

        try {

            userCharacterModel.setUser_character_id(generateUserCharacterId());
            userCharacterRepository.insertUserCharacter(userCharacterModel);

        } catch (Exception e) {
            log.error("Error at UserCharacterService insertUserCharacter", e);
            throw new RuntimeException(e);
        }

        return  userCharacterModel.getUser_character_id();

    }

    public String generateUserCharacterId(){
        String newUserCharacterId = null;

        try {

            if(userCharacterRepository.checkHasValue() == false){
                newUserCharacterId = "UC001";
            } else {
                String lastUserId = userCharacterRepository.getLastUserCharacterId();
                newUserCharacterId = Utility.generateNextId(lastUserId);
            }

        } catch (Exception e) {
            log.error("Error at userCharacterService generateUserCharacterId", e);
            throw new RuntimeException(e);
        }

        return newUserCharacterId;
    }

}
