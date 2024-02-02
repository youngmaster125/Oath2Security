package org.sid.Oath2Security.service;



import java.io.UnsupportedEncodingException;
import java.util.List;


import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.entities.ChangePassword;

import jakarta.mail.MessagingException;

public interface AccountService  {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void  addRoleToUser(String username,String rolename);
    AppUser loadUserByUsername(String username);
    public AppUser loadUserByEmail(String email);
    List<AppUser> listUsers();
    void sendVerificationEmail(AppUser appUser,String urlSite) throws UnsupportedEncodingException,MessagingException;
    boolean verify(String verificationCode);
    public void generateOneTimePassword(AppUser user);

    public int  verifyOtp(AppUser user , String otp);
    AppUser changePassword(AppUser appUser,ChangePassword changePassword);
    AppUser resetPassword(AppUser appUser,String password);


     
}
