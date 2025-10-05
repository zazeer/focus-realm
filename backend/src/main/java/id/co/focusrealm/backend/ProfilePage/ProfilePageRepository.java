package id.co.focusrealm.backend.ProfilePage;

import id.co.focusrealm.backend.GalleryPage.GalleryPageModel;
import id.co.focusrealm.backend.User.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProfilePageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProfilePageModel fetchUserData(ProfilePageModel profilePageModel){

        try {

            String fetchUserDataSql = "SELECT us.username, us.email, c.file_name\n" +
                    "FROM \"User\" us \n" +
                    "JOIN usercharacter uc ON us.user_id = uc.user_id\n" +
                    "JOIN character c ON uc.character_id = c.character_id\n" +
                    "WHERE us.user_id = ? AND uc.chosen_character = true";

            ProfilePageModel profilePageModelTemp = jdbcTemplate.queryForObject(fetchUserDataSql, new Object[]{profilePageModel.getUser_id()},
                    (rs, rowNum) -> {
                        ProfilePageModel temp = new ProfilePageModel();
                        temp.setUsername(rs.getString("username"));
                        temp.setEmail(rs.getString("email"));
                        temp.setProfile_picture_file_name(rs.getString("file_name"));
                        return temp;
                    }
            );

            return profilePageModelTemp;

        } catch (Exception e) {
            log.error("ERROR At ProfilePageRepository, fetchUserData");
            throw new RuntimeException(e);
        }

    }

    public void updateUserName(ProfilePageModel profilePageModel){

        try {
            String updateUserNameSql = "UPDATE \"User\"\n" +
                    "SET username = ?\n" +
                    "WHERE user_id = ?;";

            jdbcTemplate.update(updateUserNameSql, profilePageModel.getUsername(), profilePageModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at ProfilePageRepository, updateUserName");
            throw new RuntimeException(e);
        }

    }

    public void updateUserPassword(ProfilePageModel profilePageModel){

        try {
            String updateUserPasswordSql = "UPDATE \"User\"\n" +
                    "SET password = ?\n" +
                    "WHERE user_id = ?";

            jdbcTemplate.update(updateUserPasswordSql, profilePageModel.getPassword(), profilePageModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at ProfilePageRepository, updateUserPassword");
            throw new RuntimeException(e);
        }

    }

    public void updateEmail(ProfilePageModel profilePageModel){

        try {
            String updateEmailSql = "UPDATE \"User\"\n" +
                    "SET email = ?\n" +
                    "WHERE user_id = ?";

            jdbcTemplate.update(updateEmailSql, profilePageModel.getEmail(), profilePageModel.getUser_id());

        } catch (Exception e) {
            log.error("Error at ProfilePageRepository, updateEmail");
            throw new RuntimeException(e);
        }

    }

}
