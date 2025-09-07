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
            customizationPageRepository.fetchCustomizationPageById(customizationPageModel);

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

}
