package com.ehas.content.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.entity.UserRoleEntityKey;

import jakarta.transaction.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntityKey>{
    @Query("""
    	    SELECT ur
    	    FROM UserRoleEntity ur
    	    LEFT JOIN FETCH ur.role
    	    WHERE ur.userSeq = :seq
    		""")
    List<UserRoleEntity> findByUserSeq(@Param("seq")Integer seq);
	
	@Transactional
	@Modifying
	@Query(value="""
			DELETE FROM user_role 
			WHERE user_seq = (SELECT seq 
								FROM user 
							   WHERE id = :userId)
			  AND role_seq = :roleSeq
			""", nativeQuery = true)
	int findByUserIdAndRoleSeq(String userId, Integer roleSeq);
}
