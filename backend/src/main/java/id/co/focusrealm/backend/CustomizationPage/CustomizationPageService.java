package id.co.focusrealm.backend.CustomizationPage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomizationPageService {

    @Autowired
    private CustomizationPageRepository customizationPageRepository;

    public CustomizationPageResponse fetchCustomizationPageById(CustomizationPageModel customizationPageModel){
        CustomizationPageResponse customizationPageResponse = new CustomizationPageResponse();

        try {
            setCustomizationPageByID(customizationPageModel);

            customizationPageResponse.setCustomizationPageModel(customizationPageModel);
            customizationPageResponse.setErrorCode("200");
            customizationPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            customizationPageResponse.setErrorCode("500");
            customizationPageResponse.setErrorMessage("Error");
            log.error("Error At CustomizationPageService fetchCustomizationPageById");
            throw new RuntimeException(e);
        }

        return customizationPageResponse;
    }

    public CustomizationPageResponse updateUserSettings(CustomizationPageModel customizationPageModel){
        CustomizationPageResponse customizationPageResponse = new CustomizationPageResponse();

        try {
            customizationPageRepository.updateUserSettings(customizationPageModel);

            log.info("CHARACTER ID " + customizationPageModel.getNewUsedCharacterId());
            log.info("SCENERY ID " + customizationPageModel.getNewUsedSceneryId());

            fetchCustomizationPageById(customizationPageModel);

            customizationPageResponse.setCustomizationPageModel(customizationPageModel);
            customizationPageResponse.setErrorCode("200");
            customizationPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            customizationPageResponse.setErrorCode("500");
            customizationPageResponse.setErrorMessage("Error");
            log.error("Error At CustomizationPageService fetchCustomizationPageById");
            throw new RuntimeException(e);
        }

        return customizationPageResponse;
    }

    public void setCustomizationPageByID(CustomizationPageModel customizationPageModel){
        try {
            customizationPageModel.setUnobtainedScenery(customizationPageRepository.getUnobtainedScenery(customizationPageModel.getUser_id()));
            customizationPageModel.setCurrentlyUsedScenery(customizationPageRepository.getCurrentlyUsedScenery(customizationPageModel.getUser_id()));
            customizationPageModel.setObtainedScenery(customizationPageRepository.getObtainedSceneryExcludingCurrentlyUsed(customizationPageModel.getUser_id()));

            customizationPageModel.setUnobtainedCharacter(customizationPageRepository.getUnobtainedCharacter(customizationPageModel.getUser_id()));
            customizationPageModel.setCurrentlyUsedCharacter(customizationPageRepository.getCurrentlyUsedCharacter(customizationPageModel.getUser_id()));
            customizationPageModel.setObtainedCharacter(customizationPageRepository.getObtainedCharacterExcludingCurrentlyUsed(customizationPageModel.getUser_id()));

        } catch (Exception e) {
            log.error("Error at CustomizationPageService fetchCustomizationPageById", e);
            throw new RuntimeException(e);
        }
    }

}
