package com.qcadoo.mes.orders.states;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.mes.newstates.BasicStateService;
import com.qcadoo.mes.orders.constants.OrderFields;
import com.qcadoo.mes.orders.constants.ScheduleFields;
import com.qcadoo.mes.orders.constants.ScheduleStateChangeFields;
import com.qcadoo.mes.orders.states.constants.OrderStateStringValues;
import com.qcadoo.mes.orders.states.constants.ScheduleStateStringValues;
import com.qcadoo.mes.states.StateChangeEntityDescriber;
import com.qcadoo.model.api.Entity;

@Service
public class ScheduleStateService extends BasicStateService implements ScheduleServiceMarker {

    @Autowired
    private ScheduleStateChangeDescriber scheduleStateChangeDescriber;

    @Override
    public StateChangeEntityDescriber getChangeEntityDescriber() {
        return scheduleStateChangeDescriber;
    }

    @Override
    public Entity onValidate(Entity entity, String sourceState, String targetState, Entity stateChangeEntity,
            StateChangeEntityDescriber describer) {
        if (ScheduleStateStringValues.APPROVED.equals(targetState)) {
            checkIfScheduleHasNotPositions(entity);
            checkOrderStates(entity);
        }

        return entity;
    }

    private void checkOrderStates(Entity entity) {
        List<Entity> orders = entity.getManyToManyField(ScheduleFields.ORDERS);
        for (Entity order : orders) {
            if (!OrderStateStringValues.PENDING.equals(order.getStringField(OrderFields.STATE))
                    && !OrderStateStringValues.ACCEPTED.equals(order.getStringField(OrderFields.STATE))) {
                entity.addGlobalError("orders.schedule.orders.wrongState");
                break;
            }
        }
    }

    private void checkIfScheduleHasNotPositions(final Entity entity) {
        if (entity.getHasManyField(ScheduleFields.POSITIONS).isEmpty()) {
            entity.addGlobalError("orders.schedule.positions.isEmpty");
        }
    }

    @Override
    public Entity onBeforeSave(Entity entity, String sourceState, String targetState, Entity stateChangeEntity,
            StateChangeEntityDescriber describer) {
        if (ScheduleStateStringValues.APPROVED.equals(targetState)) {
            entity.setField(ScheduleFields.APPROVE_TIME, stateChangeEntity.getDateField(ScheduleStateChangeFields.DATE_AND_TIME));
        }

        return entity;
    }

}
