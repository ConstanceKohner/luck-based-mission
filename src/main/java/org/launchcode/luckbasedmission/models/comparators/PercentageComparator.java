package org.launchcode.luckbasedmission.models.comparators;

import org.launchcode.luckbasedmission.models.ScratcherGame;

import java.util.Comparator;

/**
 * Created by catub on 8/13/2017.
 */
public class PercentageComparator implements Comparator<ScratcherGame> {
    @Override
    public int compare(ScratcherGame o1, ScratcherGame o2) {
        return Double.compare(o2.getReturnPercentage(), o1.getReturnPercentage());
    }
}