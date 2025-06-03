package com.ehas.content.content.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ehas.content.content.base.DateEntityBase;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name ="content")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = "users")
public class ContentEntity extends DateEntityBase{
    @Id
    private Integer seq;

    @Column(nullable = false, length = 100)
    private String contentName;
    @Column(nullable = false, length = 500)
    private String contentDept;
    
    @Column(name = "used", nullable = false)
    private Boolean used;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "content")
    @Builder.Default
    private List<ContentUserEntity> users = new ArrayList<ContentUserEntity>();
}
