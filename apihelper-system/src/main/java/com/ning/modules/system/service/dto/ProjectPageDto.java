package com.ning.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProjectPageDto extends PageUtilDto implements Serializable {

    private String searchCont;
}
