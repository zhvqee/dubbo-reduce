package org.qee.cloud.spring.test;

import org.qee.cloud.test.annotation.CloudService;

@CloudService(id = "userServiceImplCloudService")
public class UserServiceImpl implements UserService {
    @Override
    public String getUser(long userId) {
        return "name:" + userId;
    }
}
