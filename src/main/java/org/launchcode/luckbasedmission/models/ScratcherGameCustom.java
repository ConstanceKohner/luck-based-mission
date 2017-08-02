package org.launchcode.luckbasedmission.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * Created by catub on 7/26/2017.
 */
@Entity
public class ScratcherGameCustom extends ScratcherGame {

    @ManyToOne
    private ScratcherGameOverview overviewGame;

    public ScratcherGameCustom() {}

    public ScratcherGameOverview getOverviewGame() {
        return overviewGame;
    }

    public void setOverviewGame(ScratcherGameOverview overviewGame) {
        this.overviewGame = overviewGame;
    }
}
