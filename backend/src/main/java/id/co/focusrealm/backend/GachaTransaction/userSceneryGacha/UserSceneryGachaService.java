package id.co.focusrealm.backend.GachaTransaction.userSceneryGacha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSceneryGachaService {

    @Autowired
    private UserSceneryGachaRepository userSceneryGachaRepository;

    public void insertUserSceneryGacha(String gacha_transaction_id, String user_scenery_id){
        UserSceneryGachaModel userSceneryGachaModel = new UserSceneryGachaModel();

        try {

            userSceneryGachaModel.setGacha_transaction_id(gacha_transaction_id);
            userSceneryGachaModel.setUser_scenery_id(user_scenery_id);

            userSceneryGachaRepository.insertUserSceneryGacha(userSceneryGachaModel);

        } catch (Exception e) {
            log.info("Error at UserSceneryGachaService insertUserSceneryGacha", e);
            throw new RuntimeException(e);
        }
    }

}
