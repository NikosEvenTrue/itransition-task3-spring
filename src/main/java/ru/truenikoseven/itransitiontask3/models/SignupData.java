package ru.truenikoseven.itransitiontask3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignupData {
    private String name;
    private String email;
    private Date lastActivity;
    private String pass_word;
}
