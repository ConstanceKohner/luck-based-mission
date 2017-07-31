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

    private int creationDay;

    private int creationMonth;

    private int creationYear;

    public ScratcherGameCustom() {}

    public ScratcherGameOverview getOverviewGame() {
        return overviewGame;
    }

    public void setOverviewGame(ScratcherGameOverview overviewGame) {
        this.overviewGame = overviewGame;
    }

    public int getCreationDay() {
        return creationDay;
    }

    public void setCreationDay(int creationDay) {
        this.creationDay = creationDay;
    }

    public int getCreationMonth() {
        return creationMonth;
    }

    public void setCreationMonth(int creationMonth) {
        this.creationMonth = creationMonth;
    }

    public int getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(int creationYear) {
        this.creationYear = creationYear;
    }

    public LocalDate getCreationDate() {
        return LocalDate.of(this.creationYear, this.creationMonth, this.creationDay);
    }
}
