package com.txnoryx.backend.fraud;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RiskFactor {

    private String factor;

    private int score;

    private String explanation;

    public RiskFactor(String factor, int score, String explanation) {
        this.factor = factor;
        this.score = score;
        this.explanation = explanation;
    }

    public String getFactor() { return factor; }
    public int getScore() { return score; }
    public String getExplanation() { return explanation; }
}