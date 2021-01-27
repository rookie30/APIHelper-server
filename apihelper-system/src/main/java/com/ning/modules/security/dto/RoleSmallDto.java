package com.ning.modules.security.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleSmallDto implements Serializable {

    private int id;

    private String name;
}
