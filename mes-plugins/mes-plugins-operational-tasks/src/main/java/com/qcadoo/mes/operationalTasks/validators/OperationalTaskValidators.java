/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
 * Version: 1.4
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.operationalTasks.validators;

import com.qcadoo.mes.operationalTasks.OperationalTasksService;
import com.qcadoo.mes.operationalTasks.constants.OperationalTaskFields;
import com.qcadoo.mes.operationalTasks.constants.OperationalTaskType;
import com.qcadoo.mes.orders.constants.OrderFields;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationalTaskValidators {

    private static final String NAME_IS_BLANK_MESSAGE = "operationalTasks.operationalTask.error.nameIsBlank";

    private static final String WRONG_DATES_ORDER_MESSAGE = "operationalTasks.operationalTask.error.finishDateIsEarlier";

    @Autowired
    private OperationalTasksService operationalTasksService;

    public boolean onValidate(final DataDefinition operationalTaskDD, final Entity operationalTask) {
        boolean isValid = true;

        isValid = hasName(operationalTaskDD, operationalTask) && isValid;
        isValid = datesAreInCorrectOrder(operationalTaskDD, operationalTask) && isValid;
        isValid = checkIfOrderHasTechnology(operationalTaskDD, operationalTask) && isValid;
        isValid = checkIfFieldSet(operationalTaskDD, operationalTask) && isValid;

        return isValid;
    }

    private boolean hasName(final DataDefinition operationalTaskDD, final Entity operationalTask) {
        String type = operationalTask.getStringField(OperationalTaskFields.TYPE);

        if (OperationalTaskType.OTHER_CASE.getStringValue().equalsIgnoreCase(type) && hasBlankName(operationalTask)) {
            operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.NAME), NAME_IS_BLANK_MESSAGE);

            return false;
        }

        return true;
    }

    private boolean hasBlankName(final Entity operationalTask) {
        return StringUtils.isBlank(operationalTask.getStringField(OperationalTaskFields.NAME));
    }

    private boolean datesAreInCorrectOrder(final DataDefinition operationalTaskDD, final Entity operationalTask) {
        Date startDate = operationalTask.getDateField(OperationalTaskFields.START_DATE);
        Date finishDate = operationalTask.getDateField(OperationalTaskFields.FINISH_DATE);

        if (finishDate.before(startDate)) {
            operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.START_DATE), WRONG_DATES_ORDER_MESSAGE);
            operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.FINISH_DATE), WRONG_DATES_ORDER_MESSAGE);

            return false;
        }

        return true;
    }

    private boolean checkIfOrderHasTechnology(final DataDefinition operationalTaskDD, final Entity operationalTask) {
        Entity order = operationalTask.getBelongsToField(OperationalTaskFields.ORDER);

        if (order == null) {
            return true;
        }

        Entity technology = order.getBelongsToField(OrderFields.TECHNOLOGY);

        if (technology == null) {
            operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.ORDER),
                    "operationalTasks.operationalTask.order.error.technologyIsNull");
            return false;
        }

        return true;
    }

    private boolean checkIfFieldSet(DataDefinition operationalTaskDD, Entity operationalTask) {
        String type = operationalTask.getStringField(OperationalTaskFields.TYPE);

        if (operationalTasksService.isOperationalTaskTypeExecutionOperationInOrder(type)) {
            boolean valid = true;
            Entity order = operationalTask.getBelongsToField(OperationalTaskFields.ORDER);
            if (Objects.isNull(order)) {
                operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.ORDER), "qcadooView.validate.field.error.missing");
                valid = false;
            }

            Entity toc = operationalTask.getBelongsToField(OperationalTaskFields.TECHNOLOGY_OPERATION_COMPONENT);
            if (Objects.isNull(toc)) {
                operationalTask.addError(operationalTaskDD.getField(OperationalTaskFields.TECHNOLOGY_OPERATION_COMPONENT), "qcadooView.validate.field.error.missing");
                valid = false;
            }
            return valid;
        }
        return true;
    }


}
