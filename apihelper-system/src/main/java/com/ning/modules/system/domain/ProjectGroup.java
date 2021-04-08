package com.ning.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.sql.Update;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "sys_group")
public class ProjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @Column(name = "group_name")
    @ApiModelProperty(value = "小组名称")
    private String groupName;

    @ApiModelProperty(value = "组长")
    private String leader;

    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "所属项目")
    private Long project;

    @ApiModelProperty(value = "组员")
    private String member;
}
