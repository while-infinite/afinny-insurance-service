package by.afinny.insuranceservice.entity.constant;

import lombok.Getter;

public enum FactorRate {
    K1(1.1),
    K2(1.2),
    K3(1.3);

    FactorRate(double rate) {
        this.rate = rate;
    }

    @Getter
    private final double rate;
}
