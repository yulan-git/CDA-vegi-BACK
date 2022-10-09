package com.vegi.vegilabback.model.enums;

public enum StatusEnum {
    EN_ATTENTE("En attente"),
    A_CORRIGER("A corriger"),
    A_SUPPRIMER("Votre recette n'a pas été validée pour la publication"),
    PUBLIEE("recette publiée");

    public final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
