package com.sxt.sys.common;

import com.sxt.sys.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data //geter seter
@AllArgsConstructor  //构造方法
@NoArgsConstructor  //无参构造方法
public class ActiverUser {
    private User user;
    private List<String > roles;
    private List<String> permissions;
}
