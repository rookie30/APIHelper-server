package com.ning.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "sys_interface_log")
@EntityListeners(AuditingEntityListener.class)
public class InterfaceLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @Column(name = "project")
    @ApiModelProperty(name = "项目id")
    private Long projectId;

    @Column(name = "interface_name")
    @ApiModelProperty(name = "接口名称")
    private String interfaceName;

    @ApiModelProperty(name = "日志内容")
    private String content;

    @ApiModelProperty(name = "操作者")
    @CreatedBy
    private String operator;

    @Column(name = "record_time")
    @ApiModelProperty(name = "记录时间")
    @LastModifiedDate
    private Date recordTime;

    @ApiModelProperty(name = "操作类型")
    private String type;

    public InterfaceLog() {
        super();
    }

    public InterfaceLog(Long projectId, String interfaceName, String content, String operator, String type) {
        this.projectId = projectId;
        this.interfaceName = interfaceName;
        this.content = content;
        this.operator = operator;
        this.type = type;
    }
}
