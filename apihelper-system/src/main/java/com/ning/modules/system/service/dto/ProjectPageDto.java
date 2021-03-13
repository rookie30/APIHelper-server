package com.ning.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectPageDto extends PageUtilDto implements Serializable {

    private String searchCont;
}
