package org.sid.Oath2Security.repos;



import org.sid.Oath2Security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
AppRole findByRoleName(String rolename);
}
