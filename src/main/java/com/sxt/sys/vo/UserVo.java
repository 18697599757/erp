package com.sxt.sys.vo;

import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends User {
    private static final long serialVersionUID=1L;


    private Integer page=1;
    private Integer limit=10;






}
