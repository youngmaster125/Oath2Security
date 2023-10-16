package org.sid.Oath2Security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.sid.Oath2Security.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl   implements UserDetailsService {
  
	private AccountService accountService;
	
	public UserDetailsServiceImpl(AccountService accountService) {
		super();
		this.accountService = accountService;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			      
	             AppUser appUser=accountService.loadUserByName(username);
	            Collection<GrantedAuthority> authorities=new ArrayList<>();
	            appUser.getAppRoles().forEach(r->{
	                authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
	            });
	             return new User(appUser.getUsername(), appUser.getPassword(),authorities );
	         
	    

	}

}
