package ru.truenikoseven.itransitiontask3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person extends PasswordlessPerson {
    private String pass_word;
}
