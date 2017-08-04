package org.launchcode.luckbasedmission.models.comparators;

import org.launchcode.luckbasedmission.models.ScratcherGame;

import java.util.Comparator;

/**
 * Created by catub on 8/4/2017.
 */
public class ReturnComparator implements Comparator<ScratcherGame> {
    @Override
    public int compare(ScratcherGame o1, ScratcherGame o2) {
        return Double.compare(o2.getExpectedReturn(), o1.getExpectedReturn());
    }
}
