package com.remembrall.ui.groceryListEntry;

import android.content.Context;

import java.util.Arrays;

public enum QuantityUnit {
    PIECE("P"), LITER("L"), MILLILITER("ML"), GRAM("G"), KILOGRAM("KG");

    private String code;

    QuantityUnit(String code) {
        this.code = code;
    }

    public static QuantityUnit fromCode(String code) {
        return Arrays.stream(values())
                     .filter(quantityUnit -> quantityUnit.code.equals(code))
                     .findFirst()
                     .orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getLabel(Context context) {
        return context.getResources()
                      .getString(context.getResources()
                                        .getIdentifier("QuantityUnit." + code,
                                                       "string",
                                                       context.getPackageName()));
    }

    public String getShortLabel(Context context) {
        return context.getResources()
                      .getString(context.getResources()
                                        .getIdentifier("QuantityUnit." + code + ".short",
                                                       "string",
                                                       context.getPackageName()));
    }
}
