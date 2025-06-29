package com.ehas.lotto.lotto.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LottoNumberStatId implements Serializable{

	private Integer round;
    private Integer number;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LottoNumberStatId)) return false;
        LottoNumberStatId id = (LottoNumberStatId) o;
        return Objects.equals(this.round, id.round) &&
               Objects.equals(this.number, id.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.round, this.number);
    }
}