package org.sid.Oath2Security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class RoleUserForm {
   private String  username;
   private String rolename;
   
public RoleUserForm() {
	super();
	// TODO Auto-generated constructor stub
}

public RoleUserForm(String username, String rolename) {
	super();
	this.username = username;
	this.rolename = rolename;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getRolename() {
	return rolename;
}

public void setRolename(String rolename) {
	this.rolename = rolename;
}
 

   
   
}
