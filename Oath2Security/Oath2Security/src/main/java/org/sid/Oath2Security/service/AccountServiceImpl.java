package org.sid.Oath2Security.service;



import java.util.List;

import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.repos.AppRoleRepository;
import org.sid.Oath2Security.repos.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private   AppUserRepository  appUserRepository;
   private   AppRoleRepository appRoleRepository;
 private PasswordEncoder passwordEncoder ;
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AppUser addNewUser(AppUser appUser) {

        String pw=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {

        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
    AppUser appUser=appUserRepository.findByUsername(username);
    AppRole appRole=appRoleRepository.findByRoleName(rolename);
     appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {

        return appUserRepository.findAll();
    }

   
}
