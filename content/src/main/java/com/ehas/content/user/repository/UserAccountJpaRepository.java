package com.ehas.content.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ehas.content.user.entity.UserAccountEntity;

import jakarta.transaction.Transactional;


public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, Integer>{
	@Transactional
    @Query(value="""
	        SELECT *
    		  FROM USER_ACCOUNT
	        WHERE user_seq = :user_seq
		    """, nativeQuery = true)
	UserAccountEntity findByUserSeq(
    		@Param("user_seq") Integer user_seq
    );
	
	@Transactional
    @Modifying
    @Query(value="""
	        UPDATE USER_ACCOUNT 
	        SET BALANCE = :balance
    		   ,STATUS = :status
	        WHERE user_seq = :user_seq
		    """, nativeQuery = true)
    int updateByUserSeq(
    		@Param("user_seq") Integer user_seq,
    	    @Param("balance") Integer balance,
    	    @Param("status") Integer status
    );
	
	@Transactional
    @Modifying
    @Query(value="""
	        UPDATE USER_ACCOUNT 
	        SET BALANCE = BALANCE - :balance
	        WHERE user_seq = :user_seq
		    """, nativeQuery = true)
    int updateBalanceForPaymentByUserSeq(
    		@Param("user_seq") Integer user_seq,
    	    @Param("balance") Integer balance
    );
	
	@Transactional
    @Modifying
    @Query(value="""
	        UPDATE USER_ACCOUNT 
	        SET BALANCE = BALANCE + :balance
	        WHERE user_seq = :user_seq
		    """, nativeQuery = true)
    int updateBalanceForChargeByUserSeq(
    		@Param("user_seq") Integer user_seq,
    	    @Param("balance") Integer balance
    );
}
