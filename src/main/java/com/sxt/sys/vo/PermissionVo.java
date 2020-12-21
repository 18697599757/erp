package com.sxt.sys.vo;

import com.sxt.sys.domain.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionVo  extends Permission {
    private static final long serialVersionUID=1L;

    private Integer page=1;
    private Integer limit=10;
}
