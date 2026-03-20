package ru.yandex.practicum.commerce.interaction.warehouse.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @Size(max = 100)
    private String country;
    @Size(max = 100)
    private String city;
    @Size(max = 100)
    private String street;
    @Size(max = 20)
    private String house;
    @Size(max = 20)
    private String flat;
}
