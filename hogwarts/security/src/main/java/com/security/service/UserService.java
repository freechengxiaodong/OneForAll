package com.security.service;

import com.security.entity.Role;
import com.security.entity.User;

public interface UserService {
    public User getUserInfo(User user);

    public Role getUserRole(User user);
}
