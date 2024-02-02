package org.sid.Oath2Security.service;



import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.entities.ChangePassword;
import org.sid.Oath2Security.repos.AppRoleRepository;
import org.sid.Oath2Security.repos.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private   AppUserRepository  appUserRepository;
   private   AppRoleRepository appRoleRepository;
 private PasswordEncoder passwordEncoder ;
 
 private JavaMailSender mailSender;
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
    		PasswordEncoder passwordEncoder ,JavaMailSender mailSender) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender=mailSender;
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
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    @Override
    public AppUser loadUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }


    @Override
    public List<AppUser> listUsers() {

        return appUserRepository.findAll();
    }
    
    public void sendVerificationEmail(AppUser appUser,String urlSite) throws
    UnsupportedEncodingException,MessagingException {
String subject="Please verify your registration";
String senderName="Youngmaster CodeTeam";
String   mailContent ="<p>Dear "+appUser.getUsername() + "</p>";

mailContent +="<p>Please click the below to verify  your registration: </p>";
String verifyUrl="http://localhost:4200/verify/"+appUser.getVerificationCode();
mailContent +="<h3> <a href=\""+verifyUrl+"\">Verify </a></h3>";
mailContent +="<p>Thank you <br> The Youngmaster CodeTeam </p>";
MimeMessage message=mailSender.createMimeMessage();
MimeMessageHelper helper=new MimeMessageHelper(message);
helper.setFrom("paulmanley150.com",senderName);
helper.setTo(appUser.getEmail());
helper.setSubject(subject);
helper.setText(mailContent,true);
System.out.println(verifyUrl);
System.out.println(appUser.getVerificationCode());
mailSender.send(message);

}
    @Override
    public boolean verify(String verificationCode){
        AppUser user=this.appUserRepository.findByVerificationCode(verificationCode);
        if(user==null||user.isEnabled()){
            return false;
   }else{
            user.setEnabled(true);
            this.appUserRepository.save(user);
            return true;

        }
   }


    @Override
   public void generateOneTimePassword(AppUser user) {
    
         
         //String randomCode=randomUUID.toString().replaceAll("_",  " ");
         
    	//String otp=RandomString.make(8);
    	Random nomb=new Random();
    	int otp=nomb.nextInt(100000, 1000000);
       System.out.println("Otp :"+otp);

        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
       user.setOneTimePassword(passwordEncoder.encode(String.valueOf(otp)));
       user.setOtpRequestedTime(new Date());
       this.appUserRepository.save(user);
       try {
           sentOtpEmail(user,String.valueOf(otp));
       } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(e);
       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }

   }

   private void sentOtpEmail(AppUser appUser, String otp) throws
           UnsupportedEncodingException,MessagingException {

       String subject="Here's your One-time-Password - Expire in 5 minutes ";
       String senderName="Youngmaster CodeTeam";
       String   mailContent ="<p>Hello "+appUser.getUsername() + "</p>";

       mailContent +="<p>Here's your Otp to change your password: </p>";

       mailContent +="<b>"+otp+"</b> <br/>";
       mailContent +="<p> <b>Note :</b>  this otp is set to expire in 5 minutes </p>";;
       MimeMessage message=mailSender.createMimeMessage();
       MimeMessageHelper helper=new MimeMessageHelper(message);
       helper.setFrom("paulmanley150.com",senderName);
       helper.setTo(appUser.getEmail());
       helper.setSubject(subject);
       helper.setText(mailContent,true);

       mailSender.send(message);


   }
    @Override
    public int  verifyOtp(AppUser user , String otp) throws UsernameNotFoundException {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        if(user==null){
          //  throw new UsernameNotFoundException("Email not Found");
            return 3;
        }else{

            long otpRequestedTimeInMilli=user.getOtpRequestedTime().getTime();
            if(otpRequestedTimeInMilli+Otp_Valid_Duration<System.currentTimeMillis()){
                System.out.println(" Expired");
                return 0;
               //  throw  new RuntimeException("your otp has been expired");
            }else{
                if(passwordEncoder.matches(otp,user.getOneTimePassword())){
                    System.out.println("otp valid ");
                  return 1;

                }else{
                    System.out.println("otp not valid ");
                    return 2;
                    // throw  new RuntimeException("opt not valid");
                }

            }





        }





    }

    @Override
    public AppUser resetPassword(AppUser appUser,String password) {
        appUser.setPassword(this.passwordEncoder.encode(password));
        appUser.setEnabled(true);
        appUser.setOneTimePassword(null);
        appUser.setOtpRequestedTime(null);
        return this.appUserRepository.save(appUser);
    }
    
    @Override
	 public AppUser changePassword(AppUser appUser, ChangePassword changePassword) {
		// TODO Auto-generated method stub
		if(appUser.getPassword().equals(changePassword.getCurrent_password())) {
			appUser.setPassword(changePassword.getNew_password());
			return this.appUserRepository.save(appUser);
		}else {
		return	null;
		}
		
	}


    private  static  final  long Otp_Valid_Duration=20*60*1000;
	
   
}
