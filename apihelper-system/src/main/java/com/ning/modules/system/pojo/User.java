package com.ning.modules.system.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sys_user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @Email
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户性别")
    private Integer gender = 0;

    @ApiModelProperty(value = "用户角色")
    private Integer role = 0;

    @Column(name = "create_time")
    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "update_time")
    @LastModifiedDate
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled = true;

    @Column(name = "avatar_path")
    @ApiModelProperty(value = "头像地址")
    private String avatarPath;


}
