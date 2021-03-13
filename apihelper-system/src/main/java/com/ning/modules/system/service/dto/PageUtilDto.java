package com.ning.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PageUtilDto implements Serializable {

    private Integer currentPage;

    private Integer size;

    private Integer total;
}
