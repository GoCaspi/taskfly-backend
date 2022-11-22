package com.gocaspi.taskfly.user;

import java.util.List;

public interface UserRepositoryCustom {
    List findUserByEmail(String lastName);
}
