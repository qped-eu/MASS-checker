package eu.qped.java.checkers.style;

import java.util.ArrayList;

import eu.qped.framework.qf.QfUser;


public class StyleStatsModel {

    private final QfUser user;
    private final ArrayList<StyleViolation> madeViolations;
    private final int vioWeights;
    private final int improvementRate;

    public StyleStatsModel (QfUser user , ArrayList<StyleViolation> madeViolations , int vioWeights, int improvementRate){
        this.user = user;
        this.madeViolations = madeViolations;
        this.vioWeights = vioWeights;
        this.improvementRate = improvementRate;
    }


    public QfUser getUser() {
        return user;
    }

    public ArrayList<StyleViolation> getMadeViolations() {
        return madeViolations;
    }

    public int getVioWeights() {
        return vioWeights;
    }

    public int getImprovementRate() {
        return improvementRate;
    }
}
