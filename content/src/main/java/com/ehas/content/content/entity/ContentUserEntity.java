package com.ehas.content.content.entity;

import java.time.LocalDateTime;

import com.ehas.content.content.base.DateEntityBase;
import com.ehas.content.user.userstatus.UserStatus;
import com.ehas.content.user.userstatus.UserStatus.UserStatusConverter;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name ="content_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = "content")
public class ContentUserEntity extends DateEntityBase{
    @Id
    private Integer seq;
    
    private Integer contentSeq;
    private Integer userSeq;

    @Column(nullable = false, length = 100)
    private String userName;
    @Column(nullable = false, length = 500)
    private String userDept;
    
    @Convert(converter = UserStatusConverter.class)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    
    @Column(name = "deleted_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime deletedDate;
    
    @ManyToOne(fetch=FetchType.LAZY)
   	@JoinColumn(name="contentSeq", referencedColumnName="seq", insertable = false, updatable = false)
    private ContentEntity content;
}
