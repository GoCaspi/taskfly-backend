package com.gocaspi.taskfly.user;

import java.util.List;

/**
 * Interface UserRepositoryCustom
 */
public interface UserRepositoryCustom {
    /**
     *
     * @param lastName lastName of a User
     * @return lastName of the user from the found email
     */
    List<User> findUserByEmail(String lastName);
}
