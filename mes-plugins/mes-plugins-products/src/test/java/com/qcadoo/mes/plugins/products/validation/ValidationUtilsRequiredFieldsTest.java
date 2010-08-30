package com.qcadoo.mes.plugins.products.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.qcadoo.mes.core.data.beans.Entity;
import com.qcadoo.mes.core.data.definition.FieldDefinition;
import com.qcadoo.mes.core.data.definition.FieldType;
import com.qcadoo.mes.core.data.definition.FieldTypeFactory;
import com.qcadoo.mes.core.data.internal.FieldTypeFactoryImpl;

public class ValidationUtilsRequiredFieldsTest {

    private FieldTypeFactory fieldTypeFactory;

    private Entity entity;

    private List<FieldDefinition> fields;

    private ValidationService validationUtils;

    @Before
    public void init() {
        fieldTypeFactory = new FieldTypeFactoryImpl();
        entity = new Entity();
        fields = new LinkedList<FieldDefinition>();
        validationUtils = new ValidationService();
        fields.add(createFieldDefinition("testField1", fieldTypeFactory.integerType(), true));
        fields.add(createFieldDefinition("testField2", fieldTypeFactory.integerType(), true));
        fields.add(createFieldDefinition("testField3", fieldTypeFactory.integerType(), false));
    }

    @Test
    public void shouldValidateWhenAllFieldsAreFilled() {
        // given

        entity.setField("testField1", 1);
        entity.setField("testField2", 2);
        entity.setField("testField3", 3);

        // when
        ValidationResult result = validationUtils.validateRequiredFields(entity, fields);

        // then
        assertEquals(true, result.isValid());
    }

    @Test
    public void shouldValidateWhenAllRequiredFieldsAreFilled() {
        // given

        entity.setField("testField1", 1);
        entity.setField("testField2", 2);
        entity.setField("testField3", null);

        // when
        ValidationResult result = validationUtils.validateRequiredFields(entity, fields);

        // then
        assertEquals(true, result.isValid());
    }

    @Test
    public void shouldNotValidateWhenNoneRequiredFieldsAreFilled() {
        // given

        entity.setField("testField1", null);
        entity.setField("testField2", null);
        entity.setField("testField3", null);

        // when
        ValidationResult result = validationUtils.validateRequiredFields(entity, fields);

        // then
        assertEquals(false, result.isValid());
        assertEquals("form.validate.nullMandatoryFieldsValidateMessage", result.getGlobalMessage());
        assertEquals("form.validate.nullFieldValidateMessage", result.getFieldMessages().get("testField1"));
        assertEquals("form.validate.nullFieldValidateMessage", result.getFieldMessages().get("testField2"));
        assertNull(result.getFieldMessages().get("testField3"));
        assertEquals(2, result.getFieldMessages().size());
    }

    @Test
    public void shouldNotValidateWhenSomeRequiredFieldsAreNotFilled() {
        // given

        entity.setField("testField1", 1);
        entity.setField("testField2", null);
        entity.setField("testField3", null);

        // when
        ValidationResult result = validationUtils.validateRequiredFields(entity, fields);

        // then
        assertEquals(false, result.isValid());
        assertEquals("form.validate.nullMandatoryFieldsValidateMessage", result.getGlobalMessage());
        assertNull(result.getFieldMessages().get("testField1"));
        assertEquals("form.validate.nullFieldValidateMessage", result.getFieldMessages().get("testField2"));
        assertNull(result.getFieldMessages().get("testField3"));
        assertEquals(1, result.getFieldMessages().size());
    }

    private FieldDefinition createFieldDefinition(final String name, final FieldType type, final boolean required) {
        FieldDefinition fieldDefinition = new FieldDefinition(name);
        fieldDefinition.setType(type);
        fieldDefinition.setRequired(required);
        return fieldDefinition;
    }
}
