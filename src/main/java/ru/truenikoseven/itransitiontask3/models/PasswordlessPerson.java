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
public class PasswordlessPerson {
    private int id;
    private String name;
    private String email;
    private Date registeredDate;
    private Date lastActivity;
    private boolean isBlocked;

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
