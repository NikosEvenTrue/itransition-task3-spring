package ru.truenikoseven.itransitiontask3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SetIsBlockedToPeopleData {
    private boolean isBlocked;
    private int[] ids;

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
