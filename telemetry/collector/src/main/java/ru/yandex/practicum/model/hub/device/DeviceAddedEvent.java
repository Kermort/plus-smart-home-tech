package ru.yandex.practicum.model.hub.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.DeviceType;
import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.model.hub.HubEvent;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {
    @NotNull
    private DeviceType deviceType;

    @NotBlank
    private String id;

    @NotNull
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
