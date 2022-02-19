package ru.truenikoseven.itransitiontask3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginData {
    private String email;
    private String pass_word;

    public LoginData(String email, String pass_word) {
        this.email = email;
        this.pass_word = pass_word;
    }
}
