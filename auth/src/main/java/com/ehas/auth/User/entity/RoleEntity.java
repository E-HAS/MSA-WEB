package com.ehas.auth.User.entity;

import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="role")
public class RoleEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    //@Column(name = "user_role", nullable = false, length = 30)
    private String roleName;

    //@Column(name = "user_role_dept", nullable = false, length = 200)
    private String roleDept;
    
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "content_seq", nullable = false)
    //private ContentEntity content;
}
