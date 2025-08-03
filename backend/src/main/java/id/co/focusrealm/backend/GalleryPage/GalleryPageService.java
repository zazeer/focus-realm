package id.co.focusrealm.backend.GalleryPage;

import id.co.focusrealm.backend.HomePage.HomePageModel;
import id.co.focusrealm.backend.HomePage.HomePageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GalleryPageService {

    @Autowired
    private GalleryPageRepository galleryPageRepository;

    public GalleryPageResponse fetchGalleryPageByUserId(GalleryPageModel galleryPageModel){
        GalleryPageResponse galleryPageResponse = new GalleryPageResponse();

        try {

            galleryPageRepository.fetchGalleryPageByUserId(galleryPageModel);

            galleryPageResponse.setGalleryPageModel(galleryPageModel);
            galleryPageResponse.setErrorCode("200");
            galleryPageResponse.setErrorMessage("Success");

        } catch (Exception e) {
            galleryPageResponse.setErrorCode("500");
            galleryPageResponse.setErrorMessage("Error");
            log.error("Error At HomePageService.fetchHomePageDataByUserId");
            throw new RuntimeException(e);
        }

        return galleryPageResponse;
    }

}
