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

    private int snapshotDay;

    private int snapshotMonth;

    private int snapshotYear;

    public ScratcherGameSnapshot() {}

    public void setOverviewGame(ScratcherGameOverview overviewGame) {
        this.overviewGame = overviewGame;
    }

    public void setSnapshotDay(int snapshotDay) {
        this.snapshotDay = snapshotDay;
    }

    public void setSnapshotMonth(int snapshotMonth) {
        this.snapshotMonth = snapshotMonth;
    }

    public void setSnapshotYear(int snapshotYear) {
        this.snapshotYear = snapshotYear;
    }

    public ScratcherGameOverview getOverviewGame() {
        return overviewGame;
    }

    public int getSnapshotDay() {
        return snapshotDay;
    }

    public int getSnapshotMonth() {
        return snapshotMonth;
    }

    public int getSnapshotYear() {
        return snapshotYear;
    }

    public LocalDate getSnapshotDate() {
        return LocalDate.of(this.snapshotYear, this.snapshotMonth, this.snapshotDay);
    }
}
