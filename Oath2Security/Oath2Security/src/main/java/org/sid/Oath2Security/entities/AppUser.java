package org.sid.Oath2Security.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
@Entity
@Data
//@AllArgsConstructor @NoArgsConstructor
public class AppUser  implements UserDetails {
   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   
   private  Long id;
   private String username;
   private  String email;
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  String password;
   private String verificationCode;
   @ManyToMany(fetch = FetchType.EAGER)
   private Collection<AppRole> appRoles=new ArrayList<>();
   private  boolean enabled;
   private String oneTimePassword;
   private Date otpRequestedTime;
	
	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public AppUser(Long id, String username, String email, String password, String verificationCode,
			Collection<AppRole> appRoles, boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.verificationCode = verificationCode;
		this.appRoles = appRoles;
		this.enabled = enabled;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<AppRole> getAppRoles() {
		return appRoles;
	}
	public void setAppRoles(Collection<AppRole> appRoles) {
		this.appRoles = appRoles;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getOneTimePassword() {
		return oneTimePassword;
	}
	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	public Date getOtpRequestedTime() {
		return otpRequestedTime;
	}
	public void setOtpRequestedTime(Date otpRequestedTime) {
		this.otpRequestedTime = otpRequestedTime;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return (Collection<? extends GrantedAuthority>) this.getAppRoles();
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	
    
	  @Override
	    public boolean isEnabled() {
	        // TODO Auto-generated method stub
	        return enabled;
	    }

    
    
}
