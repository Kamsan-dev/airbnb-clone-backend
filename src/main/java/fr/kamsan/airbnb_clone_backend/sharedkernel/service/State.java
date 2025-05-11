package fr.kamsan.airbnb_clone_backend.sharedkernel.service;

import lombok.Data;

@Data
public class State<T, V> {
    private T value;
    private V error;
    private StatusNotification status;

    public State(StatusNotification status, T value, V error) {
        this.value = value;
        this.error = error;
        this.status = status;
    }

    public static <T, V> StateBuilder<T, V> builder() {
        return new StateBuilder<>();
    }
}