package com.vegi.vegilabback.model.enums;

public enum StatusEnum {
    EN_ATTENTE("En attente"),
    A_CORRIGER("A corriger"),
    PUBLIEE("publiée");

    public final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
