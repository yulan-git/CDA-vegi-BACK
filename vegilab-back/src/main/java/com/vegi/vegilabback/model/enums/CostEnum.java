package com.vegi.vegilabback.model.enums;

public enum CostEnum {
    BON_MARCHE("Bon marché"),
    RAISONNABLE("Raisonnable"),
    COUTEUX("Coûteux");

    private final String cost;

    CostEnum(String cost) {
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }
}
