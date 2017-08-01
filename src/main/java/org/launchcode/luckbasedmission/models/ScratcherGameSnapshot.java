package org.launchcode.luckbasedmission.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * Created by catub on 7/25/2017.
 */
@Entity
public class ScratcherGameSnapshot extends ScratcherGame {

    @ManyToOne
    private ScratcherGameOverview overviewGame;

    public ScratcherGameSnapshot() {}

    public void setOverviewGame(ScratcherGameOverview overviewGame) {
        this.overviewGame = overviewGame;
    }

    public ScratcherGameOverview getOverviewGame() {
        return overviewGame;
    }
}
