package com.nikozka.springapp.repository;

import com.nikozka.springapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByIin(String iin);
    void deleteByIin(String iin);
}
