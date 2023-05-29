package ilya.shin.order_microservice.api.model;

public enum Status {
    WAITING("В ожидании"),
    COOKING("Готовится"),
    READY("Выполнен"),
    CANCELED("Отменен");

    private final String status;
    Status(String s) {
        status = s;
    }

    public String getStatus() {
        return status;
    }
}
