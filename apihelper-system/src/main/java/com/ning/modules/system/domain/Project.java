package com.ning.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sys_project")
@EntityListeners(AuditingEntityListener.class)
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "项目名称不能为空")
    @ApiModelProperty(value = "项目名称")
    @Column(name = "name")
    private String projectName;

    @ApiModelProperty(value = "项目简介")
    private String introduce;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time")
    @LastModifiedDate
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @JsonIgnore
    @ManyToMany(mappedBy = "projects")
    @ApiModelProperty(value = "用户", hidden = true)
    private Set<User> users;
}
