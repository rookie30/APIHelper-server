package com.ning.modules.system.domain;

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
    private String groupName;

    private String leader;

    @NotNull(groups = Update.class)
    private Long project;

    private String member;
}
