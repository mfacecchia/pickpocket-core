package com.feis.splitnings.features.user.repository;

import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.features.user.data.User;

public interface UserRepository extends BaseRepository<User, Integer> {

    boolean existsByEmail(String email);
}
