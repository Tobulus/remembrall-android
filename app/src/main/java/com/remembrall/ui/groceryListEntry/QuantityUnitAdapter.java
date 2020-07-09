package com.remembrall.ui.groceryListEntry;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class QuantityUnitAdapter extends ArrayAdapter<QuantityUnit> {

    private Context ctx;

    public QuantityUnitAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        ctx = context;
    }

    public QuantityUnit fromCode(String code) {
        return QuantityUnit.fromCode(code);
    }

    public int getPosition(String code) {
        return fromCode(code).ordinal();
    }

    @Override
    public int getCount() {
        return QuantityUnit.values().length;
    }

    @Override
    public QuantityUnit getItem(int position) {
        return QuantityUnit.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getLabel(ctx));

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getLabel(ctx));

        return label;
    }
}
