package com.vegi.vegilabback.model.enums;

public enum UnitEnum {
    PINCEE("pincée(s)"),
    POT("pot(s)"),
    BOITE("boîte(s)"),
    CAFE("cuillère(s) à café"),
    SOUPE("cuillère(s) à soupe"),
    POIGNEE("poignée(s)"),
    GOUTTE("goutte"),
    GRAMME("g"),
    KILO("Kg"),
    LITRE("L"),
    CENTILITRE("cL"),
    MILLILITRE("mL"),
    MORCEAU("morceau");

    public final String unit;

    UnitEnum(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
