package com.ning.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_interface")
@EntityListeners(AuditingEntityListener.class)
public class MyInterface implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @ApiModelProperty(name = "接口名称")
    private String name;

    @ApiModelProperty(name = "请求地址")
    @Column(name = "request_url")
    private String requestUrl;

    @ApiModelProperty(name = "请求方式")
    @Column(name = "request_type")
    private String requestType;

    @ApiModelProperty(name = "所属项目ID")
    @Column(name = "project")
    private Long projectId;

    @ApiModelProperty(name = "接口类别")
    private String type;

    @ApiModelProperty(name = "创建者")
    private String createBy;

    @ApiModelProperty(name = "创建时间")
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty(name = "更新者")
    @Column(name = "update_by")
    @CreatedBy
    private String updateBy;

    @ApiModelProperty(name = "更新时间")
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty(name = "接口简介")
    private String introduce;

    @ApiModelProperty(name = "接口所属目录")
    private Integer directory;

    @ApiModelProperty(name = "params参数")
    private String params;

    @ApiModelProperty(name = "header参数")
    private String headers;

    @ApiModelProperty(name = "body参数")
    private String body;
}
