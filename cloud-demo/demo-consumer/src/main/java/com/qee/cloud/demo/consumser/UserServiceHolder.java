package com.qee.cloud.demo.consumser;

import com.qee.demo.api.UserService;
import org.qee.cloud.rpc.api.annotation.CloudReference;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHolder {
    @CloudReference(id = "userService", group = "*", version = "*", check = false, timeout = 100)
    private UserService userService;

    public void print(int i) {
        String user = userService.getUser(i);
        System.out.println(user);
    }

}
