package org.sid.Oath2Security.service;



import java.util.List;

import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;

public interface AccountService  {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void  addRoleToUser(String username,String rolename);
    AppUser loadUserByName(String username);
    List<AppUser> listUsers();

     
}
