package org.qee.cloud.rpc.test;

import org.qee.cloud.rpc.annotation.CloudReference;

public class Consumer {

    @CloudReference(id = "userService", group = "*", version = "1.0", mock = "org.qee.cloud.rpc.test.UserServiceMock", check = false)
    private UserService userService;

}
