package org.sid.Oath2Security;

import org.sid.Oath2Security.config.RsaKeyConfig;
import org.sid.Oath2Security.entities.AppRole;
import org.sid.Oath2Security.entities.AppUser;
import org.sid.Oath2Security.repos.AppUserRepository;
import org.sid.Oath2Security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyConfig.class)
public class Oath2SecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(Oath2SecurityApplication.class, args);
	}
	  
   @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
   @Autowired
   private AppUserRepository userRepos;
   @Bean
	CorsConfigurationSource corsConfigurationSource1() {
		  CorsConfiguration configuration = new CorsConfiguration();
		  configuration.setAllowedOrigins(Arrays.asList("*"));
		  configuration.setAllowedMethods(Arrays.asList("*"));
		 // configuration.setAllowCredentials(true);
		  configuration.setAllowedHeaders(Arrays.asList("*"));
		 //  configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		  source.registerCorsConfiguration("/**", configuration);


		  return source;

	}
   @Bean
   public JavaMailSender getJavaMailSender() {
       JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
       mailSender.setHost("smtp.gmail.com");
       mailSender.setPort(587);

       mailSender.setUsername("paulmanley150@gmail.com");
       mailSender.setPassword("uzrrxvgbnyvssktj");

       Properties props = mailSender.getJavaMailProperties();
       props.put("mail.transport.protocol", "smtp");
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.starttls.enable", "true");
      // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
       props.put("mail.debug", "true");

       return mailSender;
   }
    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {

            accountService.addNewRole(new AppRole(null,"USER"));
            accountService.addNewRole(new AppRole(null,"ADMIN"));
            accountService.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
            accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
            accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));
            
            
            UUID randomUUID = UUID.randomUUID();
      	    String randomCode=randomUUID.toString().replaceAll("_",  " ");
            
      	    Random nomb=new Random();
      	    int code=nomb.nextInt(100000,900000 );
			
			 AppUser user= accountService.addNewUser(new
			  AppUser(null,"user1","paulmanager150@gmail.com", "1234",randomCode,new
			  ArrayList<>(),false)); 
			 user.setOneTimePassword(this.passwordEncoder().encode(String.valueOf(code)));
			 user.setOtpRequestedTime(new Date());
			 this.userRepos.save(user);
			 
			 System.out.println(user.getOneTimePassword());
			 System.out.println(code);
			 
			 // System.out.println(randomCode);
			//  AppUser appUser= accountService.addNewUser(new
			//  AppUser(null,"manley","paulsamyb@gmail.com" ,"1234",randomCode,new
			//  ArrayList<>(),false));
			 //   accountService.addRoleToUser("manley","ADMIN");
			 //   accountService.addRoleToUser("manley","USER");
	            accountService.addRoleToUser("user1","USER");
	           
			 
			 
/*
			
			  accountService.addNewUser(new AppUser(null,"user1",null,"1234",new
			  ArrayList<>())); accountService.addNewUser(new
			  AppUser(null,"admin",null,"1234",new ArrayList<>()));
			  accountService.addNewUser(new AppUser(null,"user2",null,"1234",new
			  ArrayList<>())); accountService.addNewUser(new
			  AppUser(null,"user3",null,"1234",new ArrayList<>()));
			  accountService.addNewUser(new AppUser(null,"user4",null,"1234",new
			  ArrayList<>()));
			  
			             accountService.addRoleToUser("admin","USER");
            accountService.addRoleToUser("admin","ADMIN");
            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("user1","ADMIN");
            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("user2","CUSTOMER_MANAGER");
            accountService.addRoleToUser("user3","USER");
            accountService.addRoleToUser("user3","PRODUCT_MANAGER");
            accountService.addRoleToUser("user4","USER");
            accountService.addRoleToUser("user4","BILLS_MANAGER");
*/
        };

    }
}
