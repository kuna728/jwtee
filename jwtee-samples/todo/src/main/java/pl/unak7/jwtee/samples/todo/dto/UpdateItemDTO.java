package pl.unak7.jwtee.samples.todo.dto;

import lombok.Getter;
import lombok.Setter;

public class UpdateItemDTO {
    private boolean done;

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
