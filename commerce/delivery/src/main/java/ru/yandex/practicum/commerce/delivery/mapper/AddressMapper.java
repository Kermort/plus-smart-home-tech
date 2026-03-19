package ru.yandex.practicum.commerce.delivery.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.delivery.model.Address;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.AddressDto;

@UtilityClass
public class AddressMapper {
    public Address toModel(AddressDto dto) {
        return Address.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .street(dto.getStreet())
                .house(dto.getHouse())
                .flat(dto.getFlat())
                .build();
    }

    public AddressDto toDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }
}
