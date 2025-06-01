package com.ehas.content.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ehas.content.user.userstatus.UserStatus;
import com.ehas.content.user.userstatus.UserStatus.UserStatusConverter;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = "roles")
public class UserEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(nullable = false, length = 100)
    private String id;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Convert(converter = UserStatusConverter.class)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    
    private Integer addressSeq;

    @Column(name = "password_updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime passwordUpdatedDate;

    @Column(name = "registered_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime registeredDate;

    @Column(name = "updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime deletedDate;
    
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "user") // mappedBy= 프로퍼티 이름
    //@JoinColumn(name="seq", referencedColumnName="userSeq", insertable = false, updatable = false)//상대 객체 검색 컬럼 name=, 내 객체 검색 컬럼 referencedColumnName=
    private List<UserRoleEntity> roles = new ArrayList<UserRoleEntity>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<String> lists = new ArrayList<String>();
    	this.roles.forEach(v -> { 
    		lists.add(v.getRole().getRoleName());
    	});
    	
        return  lists
        		.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.id;
    }
    
	@Override
	public String getPassword() {
		return this.password;
	}

	// 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        if(this.status == UserStatus.ACCOUNT_EXPIRED){
            return false;
        }
        return true;
    }

    // 잠긴 계정
    @Override
    public boolean isAccountNonLocked() {
        if(this.status == UserStatus.ACCOUNT_LOCKED){
            return false;
        }
        return true;
    }

    // 패스워드 만료
    @Override
    public boolean isCredentialsNonExpired() {
        if(this.status == UserStatus.CREDENTIALS_EXPIRED){
            return false;
        }
        return true;
    }
    @Override
    public boolean isEnabled() {
        if(isCredentialsNonExpired() && isAccountNonExpired() && isAccountNonLocked()){
            return true;
        }
        return false;
    }

}