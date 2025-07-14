package id.co.focusrealm.backend.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/insert_user")
    public @ResponseBody UserResponse insertUserResponse(@RequestBody UserModel user) {
        try {
            return userService.insertUser(user);
        } catch (Exception e) {
            log.error("Error At UserController InsertUserResponse");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/fetch_user")
    public @ResponseBody UserResponse fetchUserResponse(@RequestBody UserModel user) {
        try {
            return userService.fetchUser(user);
        } catch (Exception e) {
            log.error("Error At UserController FetchUserResponse");
            throw new RuntimeException(e);
        }
    }

}
