package com.ning.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Data
@Table(name = "sys_notice")
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "消息状态")
    private Integer type = 0;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "消息接收者")
    private String recipient;

    public Notice() {
        super();
    }

    public Notice(String title, String content, String createBy, String recipient) {
        this.title = title;
        this.content = content;
        this.createBy = createBy;
        this.recipient = recipient;
    }
}
