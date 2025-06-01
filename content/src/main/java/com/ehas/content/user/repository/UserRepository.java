package com.ehas.content.user.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.userstatus.UserStatus;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	@Query(value="SELECT * FROM USER WHERE seq = :seq", nativeQuery = true)
	UserEntity findBySeq(@Param("seq")Integer seq);
	
	@Query(value="SELECT * FROM USER WHERE id = :id", nativeQuery = true)
	UserEntity findById(@Param("id")String id);
	
	@Query(value="""
			SELECT SEQ
				  ,NAME
				  ,STATUS
				  ,ADDRESS_SEQ
			 FROM USER 
			WHERE id = :id
			""", nativeQuery = true)
	UserDto getUserById(@Param("id")String id);
	
	@Query(value=   " SELECT * "
			+ " FROM USER "
			+ " WHERE status = :status "
			+ " AND id = :id", nativeQuery = true)
	UserEntity findByStatusAndId(@Param("status")String status, @Param("id")String id);
	
	@Transactional
    @Modifying
    @Query(value="""
	        UPDATE user 
	        SET 
	            name = :name,
	            password = :password,
	            status = :status,
	            address_seq = :addressSeq,
	            password_updated_date = :passwordUpdatedDate,
	            updated_date = :updatedDate,
	            deleted_date = :deletedDate
	        WHERE seq = :seq
		    """, nativeQuery = true)
    int updateUserBySeq(
    	    @Param("seq") Integer seq,
    	    @Param("name") String name,
    	    @Param("password") String password,
    	    @Param("status") UserStatus status,
    	    @Param("addressSeq") Integer addressSeq,
    	    @Param("passwordUpdatedDate") LocalDateTime passwordUpdatedDate,
    	    @Param("updatedDate") LocalDateTime updatedDate,
    	    @Param("deletedDate") LocalDateTime deletedDate
    );
}
