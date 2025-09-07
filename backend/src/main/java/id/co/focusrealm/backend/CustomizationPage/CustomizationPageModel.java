package id.co.focusrealm.backend.CustomizationPage;

import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.ObtainedSceneryModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModelPackage.UnobtainedSceneryModel;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CustomizationPageModel {

    private String user_id;
    private String newUsedCharacterId;
    private String newUsedSceneryId;

    private ObtainedCharacterModel currentlyUsedCharacter;
    private ObtainedSceneryModel currentlyUsedScenery;

    private ArrayList<UnobtainedCharacterModel> unobtainedCharacter;
    private ArrayList<UnobtainedSceneryModel>  unobtainedScenery;
    private ArrayList<ObtainedCharacterModel> obtainedCharacter;
    private ArrayList<ObtainedSceneryModel> obtainedScenery;

}
