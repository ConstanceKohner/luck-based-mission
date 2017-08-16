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
    private ScratcherGame associatedGame;

    public ScratcherGameCustom() {}

    public ScratcherGame getAssociatedGame() {
        return associatedGame;
    }

    public void setAssociatedGame(ScratcherGame associatedGame) {
        this.associatedGame = associatedGame;
    }
}
