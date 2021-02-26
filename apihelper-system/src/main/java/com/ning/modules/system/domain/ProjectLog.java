package com.ning.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_project_log")
@EntityListeners(AuditingEntityListener.class)
public class ProjectLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @ApiModelProperty(value = "操作者")
    private String operator;


    @Column(name = "record_time")
    @ApiModelProperty(value = "记录时间")
    @LastModifiedDate
    private Date recordTime;

    @ApiModelProperty(value = "记录内容")
    private String content;

    @ApiModelProperty(value = "所属项目id")
    @Column(name = "project_id")
    private Long projectId;

    public ProjectLog(String operator, String content, Long projectId) {
        this.operator = operator;
        this.content = content;
        this.projectId = projectId;
    }
}
