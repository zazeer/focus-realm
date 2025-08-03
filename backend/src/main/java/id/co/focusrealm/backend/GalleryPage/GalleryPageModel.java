package id.co.focusrealm.backend.GalleryPage;

import id.co.focusrealm.backend.GalleryPage.ModelPackage.ObtainedCharacterModel;
import id.co.focusrealm.backend.GalleryPage.ModelPackage.ObtainedSceneryModel;
import id.co.focusrealm.backend.GalleryPage.ModelPackage.UnobtainedCharacterModel;
import id.co.focusrealm.backend.GalleryPage.ModelPackage.UnobtainedSceneryModel;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GalleryPageModel {

    private String user_id;

    private ArrayList<UnobtainedCharacterModel> unobtainedCharacter;
    private ArrayList<UnobtainedSceneryModel>  unobtainedScenery;
    private ArrayList<ObtainedCharacterModel> obtainedCharacter;
    private ArrayList<ObtainedSceneryModel> obtainedScenery;

}
