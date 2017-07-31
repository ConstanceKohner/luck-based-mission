package org.launchcode.luckbasedmission.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by catub on 7/26/2017.
 */
@Entity
public class ScratcherGameOverview extends ScratcherGame {

    @OneToMany
    @JoinColumn(name = "scratcher_game_uid_number")
    private List<ScratcherGameSnapshot> snapshotGames = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "scratcher_game_uid_number")
    private List<ScratcherGameCustom> customGames = new ArrayList<>();

    public ScratcherGameOverview() {
    }
}
