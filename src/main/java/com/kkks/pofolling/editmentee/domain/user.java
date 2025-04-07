package com.kkks.pofolling.editmentee.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class user {
    private long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone_number;
    private Role role;
}
