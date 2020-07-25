package com.remembrall.ui.groceryListEntry;

import android.content.Context;

import java.util.Arrays;
import java.util.Objects;

public enum QuantityUnit {
    UNDEFINED(null), PIECE("P"), LITER("L"), MILLILITER("ML"), GRAM("G"), KILOGRAM("KG");

    private String code;

    QuantityUnit(String code) {
        this.code = code;
    }

    public static QuantityUnit fromCode(String code) {
        return Arrays.stream(values())
                     .filter(quantityUnit -> Objects.equals(quantityUnit.code, code))
                     .findFirst()
                     .orElse(QuantityUnit.UNDEFINED);
    }

    public String getCode() {
        return code;
    }

    public String getLabel(Context context) {
        if (this == UNDEFINED) {
            return "";
        }

        return context.getResources()
                      .getString(context.getResources()
                                        .getIdentifier("QuantityUnit." + code,
                                                       "string",
                                                       context.getPackageName()));
    }

    public String getShortLabel(Context context) {
        if (this == UNDEFINED) {
            return "";
        }

        return context.getResources()
                      .getString(context.getResources()
                                        .getIdentifier("QuantityUnit." + code + ".short",
                                                       "string",
                                                       context.getPackageName()));
    }
}
