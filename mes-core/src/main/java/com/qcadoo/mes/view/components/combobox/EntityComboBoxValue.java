package com.qcadoo.mes.view.components.combobox;

import java.util.Map;

import com.qcadoo.mes.view.components.SimpleValue;

public final class EntityComboBoxValue extends SimpleValue {

    private Map<Long, String> values;

    public Long getSelectedValue() {
        return (Long) getValue();
    }

    public void setSelectedValue(final Long selectedValue) {
        setValue(selectedValue);
    }

    public Map<Long, String> getValues() {
        return values;
    }

    public void setValues(final Map<Long, String> values) {
        this.values = values;
    }

}
