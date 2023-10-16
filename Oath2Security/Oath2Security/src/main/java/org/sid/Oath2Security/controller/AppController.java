package org.sid.Oath2Security.controller;



import java.security.Principal;
import java.util.List;

import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.entities.RoleUserForm;
import org.sid.Oath2Security.service.AccountServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AppController {
  private   AccountServiceImpl accountService ;


    public AppController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }
     @GetMapping(path = "/users")
    public List<AppUser> listuser(){
        return  accountService.listUsers();
    }
    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path = "/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }
    @PostMapping(path = "/addRoleToUser")
    public void addRoleTouser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRolename());
    }
    @GetMapping(path="/profile")
    public AppUser profile(Principal principal) {
    	return this.accountService.loadUserByName(principal.getName());
    }
    
   
}
