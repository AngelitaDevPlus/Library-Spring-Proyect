package com.spring.library.repository;


import com.spring.library.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,String> {

    public AppUser findUserByEmail(@Param("email") String email); //No need specify query, uses Repository methods.
}
