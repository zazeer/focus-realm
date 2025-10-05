package id.co.focusrealm.backend.GachaPage;

import id.co.focusrealm.backend.CharacterSceneryModel.CharacterModel;
import id.co.focusrealm.backend.CharacterSceneryModel.SceneryModel;
import id.co.focusrealm.backend.GachaTransaction.GachaTransactionService;
import id.co.focusrealm.backend.GachaTransaction.userCharacterGacha.UserCharacterGachaService;
import id.co.focusrealm.backend.GachaTransaction.userSceneryGacha.UserSceneryGachaService;
import id.co.focusrealm.backend.Log.LogService;
import id.co.focusrealm.backend.UserCharacter.UserCharacterModel;
import id.co.focusrealm.backend.UserCharacter.UserCharacterRepository;
import id.co.focusrealm.backend.UserCharacter.UserCharacterService;
import id.co.focusrealm.backend.UserScenery.UserSceneryModel;
import id.co.focusrealm.backend.UserScenery.UserSceneryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class GachaService {

    @Autowired
    private GachaRepository gachaRepository;

    @Autowired
    private UserCharacterService userCharacterService;

    @Autowired
    private GachaTransactionService gachaTransactionService;

    @Autowired
    private UserCharacterGachaService userCharacterGachaService;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private UserSceneryService userSceneryService;

    @Autowired
    private UserSceneryGachaService userSceneryGachaService;

    private int commonChance = 69;
    private int rareChance = 30;
    private int legendaryChance = 1;

    private String currentCharacterBannerId = "CH003";
    private String currentSceneryBannerId = "SC003";

    private int legendaryDuplicateAmount = 1000;
    private int rareDuplicateAmount = 200;
    private int commonDuplicateAmount = 25;

    private int defaultGachaPrice = 100;

    public GachaPageResponse fetchGachaPageByUserId(GachaModel gachaModel){
        GachaPageResponse gachaPageResponse = new GachaPageResponse();

        try {

            GachaModel temp = gachaRepository.fetchUserDataGachaPage(gachaModel);
            gachaModel.setUser_pity(temp.getUser_pity());
            gachaModel.setUser_coins(temp.getUser_coins());

            // Ngambil Data Banner
            gachaModel.setCurrentCharacterBanner(gachaRepository.fetchCharacterData(currentCharacterBannerId));
            gachaModel.setCurrentSceneryBanner(gachaRepository.fetchSceneryData(currentSceneryBannerId));

            gachaModel.setCharacterHistoryModels(gachaRepository.getCharacterHistory(gachaModel.getUser_id()));
            gachaModel.setSceneryHistoryModels(gachaRepository.getSceneryHistory(gachaModel.getUser_id()));

            gachaPageResponse.setGachaModel(gachaModel);
            gachaPageResponse.setErrorCode("200");
            gachaPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            gachaPageResponse.setErrorCode("500");
            gachaPageResponse.setErrorMessage("Error");
            log.error("Error At GachaService fetchGachaPageByUserId");
            throw new RuntimeException(e);
        }

        return gachaPageResponse;
    }

    public GachaPageResponse doGachaCharacter(GachaModel gachaModel){
        GachaPageResponse gachaPageResponse = new GachaPageResponse();

        try {

            if(checkIsUserCoinsSufficient(gachaModel) == true){
                updateUserCoinPreGacha(gachaModel);
                gachaCharacter(gachaModel);

                // Refresh Page
                fetchGachaPageByUserId(gachaModel);

                gachaPageResponse.setGachaModel(gachaModel);
                gachaPageResponse.setErrorCode("200");
                gachaPageResponse.setErrorMessage("Success");
            } else {
                gachaPageResponse.setGachaModel(gachaModel);
                gachaPageResponse.setErrorCode("500");
                gachaPageResponse.setErrorMessage("Inssuficient Coins");

                fetchGachaPageByUserId(gachaModel);
            }

        } catch (Exception e) {
            gachaPageResponse.setErrorCode("500");
            gachaPageResponse.setErrorMessage("Error");
            log.error("Error At GachaService doGachaCharacter");
            throw new RuntimeException(e);
        }

        return gachaPageResponse;
    }

    public GachaPageResponse doGachaScenery(GachaModel gachaModel){
        GachaPageResponse gachaPageResponse = new GachaPageResponse();

        try {

            if(checkIsUserCoinsSufficient(gachaModel) == true){
                updateUserCoinPreGacha(gachaModel);
                gachaScenery(gachaModel);

                // Refresh Page
                fetchGachaPageByUserId(gachaModel);

                gachaPageResponse.setGachaModel(gachaModel);
                gachaPageResponse.setErrorCode("200");
                gachaPageResponse.setErrorMessage("Success");
            } else {

                gachaPageResponse.setGachaModel(gachaModel);
                gachaPageResponse.setErrorCode("500");
                gachaPageResponse.setErrorMessage("Inssuficient Coins");

                fetchGachaPageByUserId(gachaModel);
            }

        } catch (Exception e) {
            gachaPageResponse.setErrorCode("500");
            gachaPageResponse.setErrorMessage("Error");
            log.error("Error At GachaService doGachaScenery");
            throw new RuntimeException(e);
        }

        return gachaPageResponse;
    }

    public void gachaCharacter(GachaModel gachaModel){
        try {
            ArrayList<CharacterModel> allCharacters = gachaRepository.getAllCharacter();
            int currentUserPity = gachaRepository.getUserPity(gachaModel.getUser_id());

            ArrayList<CharacterModel> characterObtained = new ArrayList<CharacterModel>();

            for(int i = 0; i < gachaModel.getGacha_amount(); i++){
                Date created_at = (new Timestamp(System.currentTimeMillis()));
                int number = ThreadLocalRandom.current().nextInt(1, 101);
                currentUserPity = currentUserPity - 1;

                if (currentUserPity == 0) {

                    boolean checkExists = gachaRepository.checkCharacterExists(currentCharacterBannerId, gachaModel.getUser_id());

                    if(checkExists == false){

                        UserCharacterModel userCharacterModel = new UserCharacterModel();
                        userCharacterModel.setUser_id(gachaModel.getUser_id());
                        userCharacterModel.setCharacter_id(currentCharacterBannerId);
                        userCharacterModel.setAcquire_date(created_at);
                        userCharacterModel.setChosen_character(false);

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(currentCharacterBannerId);
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = userCharacterService.insertUserCharacter(userCharacterModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);

                    } else {

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(currentCharacterBannerId);
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = gachaRepository.getUserCharacterId(currentCharacterBannerId, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), legendaryDuplicateAmount);
                    }

                    currentUserPity = 100;

                } else if(number <= legendaryChance){
                    ArrayList<CharacterModel> legendaryCharacters = new ArrayList<>();
                    setCharacterBasedOnRarity(allCharacters, legendaryCharacters, "Legendary");
                    int index = ThreadLocalRandom.current().nextInt(0, legendaryCharacters.size());

                    boolean checkExists = gachaRepository.checkCharacterExists(legendaryCharacters, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        UserCharacterModel userCharacterModel = new UserCharacterModel();
                        userCharacterModel.setUser_id(gachaModel.getUser_id());
                        userCharacterModel.setCharacter_id(legendaryCharacters.get(index).getCharacter_id());
                        userCharacterModel.setAcquire_date(created_at);
                        userCharacterModel.setChosen_character(false);

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(legendaryCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = userCharacterService.insertUserCharacter(userCharacterModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);
                    } else {

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(legendaryCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = gachaRepository.getUserCharacterId(legendaryCharacters, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), legendaryDuplicateAmount);
                    }

                } else if (number <= rareChance + legendaryChance){

                    ArrayList<CharacterModel> rareCharacters = new ArrayList<>();
                    setCharacterBasedOnRarity(allCharacters, rareCharacters, "Rare");
                    int index = ThreadLocalRandom.current().nextInt(0, rareCharacters.size());

                    boolean checkExists = gachaRepository.checkCharacterExists(rareCharacters, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        UserCharacterModel userCharacterModel = new UserCharacterModel();
                        userCharacterModel.setUser_id(gachaModel.getUser_id());
                        userCharacterModel.setCharacter_id(rareCharacters.get(index).getCharacter_id());
                        userCharacterModel.setAcquire_date(created_at);
                        userCharacterModel.setChosen_character(false);

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(rareCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = userCharacterService.insertUserCharacter(userCharacterModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);
                    } else {

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(rareCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = gachaRepository.getUserCharacterId(rareCharacters, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), rareDuplicateAmount);
                    }

                } else if (number <= commonChance + rareChance + legendaryChance){

                    ArrayList<CharacterModel> commonCharacters = new ArrayList<>();
                    setCharacterBasedOnRarity(allCharacters, commonCharacters, "Common");
                    int index = ThreadLocalRandom.current().nextInt(0, commonCharacters.size());

                    boolean checkExists = gachaRepository.checkCharacterExists(commonCharacters, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        // Setting User Character Data
                        UserCharacterModel userCharacterModel = new UserCharacterModel();
                        userCharacterModel.setUser_id(gachaModel.getUser_id());
                        userCharacterModel.setCharacter_id(commonCharacters.get(index).getCharacter_id());
                        userCharacterModel.setAcquire_date(created_at);
                        userCharacterModel.setChosen_character(false);

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(commonCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = userCharacterService.insertUserCharacter(userCharacterModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);
                    } else {

                        CharacterModel obtainedCharacter = new CharacterModel();
                        obtainedCharacter = gachaRepository.fetchCharacterData(commonCharacters.get(index).getCharacter_id());
                        characterObtained.add(obtainedCharacter);

                        String lastUserCharacterId = gachaRepository.getUserCharacterId(commonCharacters, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Character", defaultGachaPrice);
                        userCharacterGachaService.insertUserCharacterGacha(lastPurchaseTransactionId, lastUserCharacterId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), commonDuplicateAmount);
                    }
                }
            }

            gachaModel.setObtainedCharacter(characterObtained);
            gachaRepository.updateUserPity(gachaModel.getUser_id(), currentUserPity);

        } catch (Exception e) {
            log.error("Error At GachaService gachaCharacter");
            throw new RuntimeException(e);
        }
    }

    public void gachaScenery(GachaModel gachaModel){
        try {
            ArrayList<SceneryModel> allScenery = gachaRepository.getAllScenery();
            int currentUserPity = gachaRepository.getUserPity(gachaModel.getUser_id());

            ArrayList<SceneryModel> sceneryObtained = new ArrayList<SceneryModel>();

            for(int i = 0; i < gachaModel.getGacha_amount(); i++){
                Date created_at = (new Timestamp(System.currentTimeMillis()));
                int number = ThreadLocalRandom.current().nextInt(1, 101);
                currentUserPity = currentUserPity - 1;

                if (currentUserPity == 0) {

                    boolean checkExists = gachaRepository.checkSceneryExists(currentSceneryBannerId, gachaModel.getUser_id());

                    if(checkExists == false){

                        UserSceneryModel userSceneryModel = new UserSceneryModel();
                        userSceneryModel.setUser_id(gachaModel.getUser_id());
                        userSceneryModel.setScenery_id(currentSceneryBannerId);
                        userSceneryModel.setAcquire_date(created_at);
                        userSceneryModel.setChosen_scenery(false);

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(currentSceneryBannerId);
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = userSceneryService.insertUserScenery(userSceneryModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);

                    } else {

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(currentSceneryBannerId);
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = gachaRepository.getUserSceneryId(currentSceneryBannerId, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), legendaryDuplicateAmount);
                    }

                    currentUserPity = 100;

                } else if(number <= legendaryChance){
                    ArrayList<SceneryModel> legendaryScenery = new ArrayList<>();
                    setSceneryBasedOnRarity(allScenery, legendaryScenery, "Legendary");
                    int index = ThreadLocalRandom.current().nextInt(0, legendaryScenery.size());

                    boolean checkExists = gachaRepository.checkSceneryExists(legendaryScenery, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        UserSceneryModel userSceneryModel = new UserSceneryModel();
                        userSceneryModel.setUser_id(gachaModel.getUser_id());
                        userSceneryModel.setScenery_id(legendaryScenery.get(index).getScenery_id());
                        userSceneryModel.setAcquire_date(created_at);
                        userSceneryModel.setChosen_scenery(false);

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(legendaryScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = userSceneryService.insertUserScenery(userSceneryModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);
                    } else {

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(legendaryScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = gachaRepository.getUserSceneryId(legendaryScenery, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), legendaryDuplicateAmount);
                    }

                } else if (number <= rareChance + legendaryChance){

                    ArrayList<SceneryModel> rareScenery = new ArrayList<>();
                    setSceneryBasedOnRarity(allScenery, rareScenery, "Rare");
                    int index = ThreadLocalRandom.current().nextInt(0, rareScenery.size());

                    boolean checkExists = gachaRepository.checkSceneryExists(rareScenery, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        UserSceneryModel userSceneryModel = new UserSceneryModel();
                        userSceneryModel.setUser_id(gachaModel.getUser_id());
                        userSceneryModel.setScenery_id(rareScenery.get(index).getScenery_id());
                        userSceneryModel.setAcquire_date(created_at);
                        userSceneryModel.setChosen_scenery(false);

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(rareScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = userSceneryService.insertUserScenery(userSceneryModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);
                    } else {

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(rareScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = gachaRepository.getUserSceneryId(rareScenery, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), rareDuplicateAmount);
                    }

                } else if (number <= commonChance + rareChance + legendaryChance){

                    ArrayList<SceneryModel> commonScenery = new ArrayList<>();
                    setSceneryBasedOnRarity(allScenery, commonScenery, "Common");
                    int index = ThreadLocalRandom.current().nextInt(0, commonScenery.size());

                    boolean checkExists = gachaRepository.checkSceneryExists(commonScenery, index, gachaModel.getUser_id());
                    if(checkExists == false){
                        UserSceneryModel userSceneryModel = new UserSceneryModel();
                        userSceneryModel.setUser_id(gachaModel.getUser_id());
                        userSceneryModel.setScenery_id(commonScenery.get(index).getScenery_id());
                        userSceneryModel.setAcquire_date(created_at);
                        userSceneryModel.setChosen_scenery(false);

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(commonScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = userSceneryService.insertUserScenery(userSceneryModel);
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);
                    } else {

                        SceneryModel obtainedScenery = new SceneryModel();
                        obtainedScenery = gachaRepository.fetchSceneryData(commonScenery.get(index).getScenery_id());
                        sceneryObtained.add(obtainedScenery);

                        String lastUserSceneryId = gachaRepository.getUserSceneryId(commonScenery, index, gachaModel.getUser_id());
                        String lastLogId = logService.insertLog(gachaModel.getUser_id(), "Gacha", created_at);
                        String lastPurchaseTransactionId = gachaTransactionService.insertGachaTransaction(lastLogId, "Scenery", defaultGachaPrice);
                        userSceneryGachaService.insertUserSceneryGacha(lastPurchaseTransactionId, lastUserSceneryId);

                        gachaRepository.increaseUserCoins(gachaModel.getUser_id(), commonDuplicateAmount);
                    }
                }
            }

            gachaModel.setObtainedScenery(sceneryObtained);
            gachaRepository.updateUserPity(gachaModel.getUser_id(), currentUserPity);

        } catch (Exception e) {
            log.error("Error At GachaService gachaScenery");
            throw new RuntimeException(e);
        }
    }

    public void updateUserCoinPreGacha(GachaModel gachaModel){

        try {

            int totalCoinsDeducted = gachaModel.getGacha_amount() * defaultGachaPrice;
            gachaRepository.decreaseUserCoins(gachaModel.getUser_id(), totalCoinsDeducted);

        } catch (Exception e) {
            log.error("Error At GachaService gachaCharacter");
            throw new RuntimeException(e);
        }

    }

    public void setCharacterBasedOnRarity(ArrayList<CharacterModel> allCharacter, ArrayList<CharacterModel> RarityBasedCharacter, String Rarity){
        try {
            for(int i = 0; i < allCharacter.size(); i++){
                if(allCharacter.get(i).getCharacter_rarity().equalsIgnoreCase(Rarity)){
                    CharacterModel temp = new CharacterModel();
                    temp = allCharacter.get(i);
                    RarityBasedCharacter.add(temp);
                }
            }

        } catch (Exception e) {
            log.error("Error At GachaService setCharacterBasedOnRarity");
            throw new RuntimeException(e);
        }
    }

    public void setSceneryBasedOnRarity(ArrayList<SceneryModel> allScenery, ArrayList<SceneryModel> RarityBasedScenery, String Rarity){
        try {
            for(int i = 0; i < allScenery.size(); i++){
                if(allScenery.get(i).getScenery_rarity().equalsIgnoreCase(Rarity)){
                    SceneryModel temp = new SceneryModel();
                    temp = allScenery.get(i);
                    RarityBasedScenery.add(temp);
                }
            }

        } catch (Exception e) {
            log.error("Error At GachaService setSceneryBasedOnRarity");
            throw new RuntimeException(e);
        }
    }

    public boolean checkIsUserCoinsSufficient(GachaModel gachaModel){
        try {
            int currentUserCoins = gachaRepository.getUserCoin(gachaModel.getUser_id());
            int coinsCost = gachaModel.getGacha_amount() * defaultGachaPrice;

            if(currentUserCoins < coinsCost){
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Error At GachaService checkIsUserCoinsSufficient");
            throw new RuntimeException(e);
        }
    }

}
