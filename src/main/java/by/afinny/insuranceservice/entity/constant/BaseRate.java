package by.afinny.insuranceservice.entity.constant;

import lombok.Getter;

public enum BaseRate {
    RUB_5000(5000),
    RUB_7000(7000);

    BaseRate(int rate) {
        this.rate = rate;
    }

    @Getter
    private final int rate;
}
