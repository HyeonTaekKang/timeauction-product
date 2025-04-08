package com.timeAuction.timeProduct.repository.user;


import com.timeAuction.timeProduct.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
        Optional<User> findByEmail(String email);
}
