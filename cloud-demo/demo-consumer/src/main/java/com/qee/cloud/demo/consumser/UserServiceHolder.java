package com.qee.cloud.demo.consumser;

import com.qee.demo.api.UserService;
import org.qee.cloud.rpc.api.annotation.CloudReference;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHolder {
    @CloudReference(id="userService",group="*",version = "*",check=false)
    private UserService userService;

    public void print() {
        String user = userService.getUser(1L);
        System.out.println(user);
    }

}
