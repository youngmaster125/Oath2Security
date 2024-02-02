package org.sid.Oath2Security.controller;



import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.sid.Oath2Security.Utility;
import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.entities.ChangePassword;
import org.sid.Oath2Security.entities.ResetPassword;
import org.sid.Oath2Security.entities.RoleUserForm;
import org.sid.Oath2Security.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;




@RestController
@CrossOrigin("*")
public class AppController {
  private   AccountServiceImpl accountService ;

  @Autowired
   ObjectMapper objectMapper;
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
    
    @PostMapping(path = "/new_user")
    public ResponseEntity< AppUser> saveUserr(@RequestBody AppUser appUser, HttpServletRequest request) throws Exception{
        AppUser username=this.accountService.loadUserByUsername(appUser.getUsername());
        AppUser email=this.accountService.loadUserByEmail(appUser.getEmail());
    	
        if(username!=null||email!=null) {
       return new ResponseEntity("username or email found",HttpStatus.FOUND);
        }else {
        appUser.setAppRoles(new ArrayList<>());
     //   appUser.setPassword("");
        String  urlSite= Utility.getSiteUrl(request);
        System.out.println(urlSite);
       appUser.setEnabled(false);
         UUID randomUUID = UUID.randomUUID();
 	    String randomCode=randomUUID.toString().replaceAll("_",  " ");
 	    appUser.setVerificationCode(randomCode);
        this.accountService.sendVerificationEmail(appUser , urlSite);
        AppUser user= accountService.addNewUser(appUser);
        
        return new ResponseEntity<>(user,HttpStatus.OK);
        
        }
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
    	return this.accountService.loadUserByUsername(principal.getName());
    }
    @GetMapping("/verify")
    public Boolean verifyAccount(@Param("code") String code) throws JsonProcessingException{
        boolean verified=this.accountService.verify(code);
        String pageTitle= verified ? "Verification Succeeded":"Verification failled";
      //  return objectMapper.writeValueAsString(pageTitle).toString();
        return verified;
    }
    @GetMapping("/sentOtp")
    public ResponseEntity<String>  sentOpt(@Param("email") String email) throws UsernameNotFoundException, JsonProcessingException {
        AppUser user=this.accountService.loadUserByEmail(email);
        if(user==null){
            System.out.println("user not found");
         return new ResponseEntity(" email  not found",HttpStatus.NOT_FOUND);
        }else{
          // this.accountService.generateOneTimePassword(user);
          return new ResponseEntity(objectMapper.writeValueAsString("an otp with six digit has sent to you please check email ").toString(),HttpStatus.OK);
        }

    }
    @GetMapping("/verifyOtp")
    public ResponseEntity<String>  verifyOtp(@Param("email") String email,@Param("otp") String otp ) throws UsernameNotFoundException, JsonProcessingException {

        AppUser user=this.accountService.loadUserByEmail(email);
        if(user==null){
          //  throw new UsernameNotFoundException("Email not Found");
        	return new ResponseEntity(" user  not found",HttpStatus.NOT_FOUND);
        }else{
          int resp=  this.accountService.verifyOtp(user,otp);
          if(resp==0) {
        	  return new ResponseEntity(" otp has been expired",HttpStatus.REQUEST_TIMEOUT); 
          }else if(resp==1) {
        	  return new ResponseEntity(objectMapper.writeValueAsString(" opt verified successfully").toString(),HttpStatus.OK); 
          }else {
        	  return new ResponseEntity("otp not valide",HttpStatus.NOT_ACCEPTABLE); 
          }
        }
	

    }

    @PostMapping("/changePassword")
    public ResponseEntity<AppUser> changePassword(@RequestBody ChangePassword changePassword){

    	  AppUser user=this.accountService.loadUserByUsername(changePassword.getUsername());
          if(user==null){

          	return new ResponseEntity(" user  not found",HttpStatus.NOT_FOUND);
          }else{
              System.out.println(changePassword);
              if(changePassword.getNew_password().equals(changePassword.getConfirm_new_password())) {
                 AppUser user1= this.accountService.changePassword(user, changePassword);
                 if(user1==null) {
                	 return new ResponseEntity("Old Password is incorrect",HttpStatus.NOT_ACCEPTABLE); 
                 }else {
                	 return new ResponseEntity(user1,HttpStatus.OK); 
                 }
              }else
                  System.out.println("Password are not same");
                  return new ResponseEntity("Password are not same",HttpStatus.CONFLICT); 
               
              }
          }
    
    @PostMapping("/resetPassword")
    public ResponseEntity<AppUser> resetPassword(@RequestBody ResetPassword resetPassword){

        AppUser user=this.accountService.loadUserByEmail(resetPassword.getEmail());
        if(user==null){

        	return new ResponseEntity(" user  not found",HttpStatus.NOT_FOUND);
        }else{
            System.out.println(resetPassword);
            if(resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
               AppUser user1= this.accountService.resetPassword(user, resetPassword.getPassword());
               return new ResponseEntity(user1,HttpStatus.OK); 
            }else{
                System.out.println("Password are not same");
                return new ResponseEntity("Password are not same",HttpStatus.CONFLICT); 
             
            }
        }
    }

 

    private  static  final  long Otp_Valid_Duration=5*60*1000;

   
}
