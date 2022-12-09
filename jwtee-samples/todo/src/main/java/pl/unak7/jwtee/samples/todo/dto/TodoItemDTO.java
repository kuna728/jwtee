package pl.unak7.jwtee.samples.todo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class TodoItemDTO {
    private long id;
    private String name;
    private boolean done;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
