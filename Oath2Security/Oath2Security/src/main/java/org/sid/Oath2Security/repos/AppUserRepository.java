package org.sid.Oath2Security.repos;

import org.sid.Oath2Security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

 import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
   AppUser findByUsername(String username);
   

  AppUser findByVerificationCode(String verificationCode);

   AppUser findByEmail(String email);


}
