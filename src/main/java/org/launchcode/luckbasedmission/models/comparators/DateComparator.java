package org.launchcode.luckbasedmission.models.comparators;

import org.launchcode.luckbasedmission.models.ScratcherGame;

import java.util.Comparator;

/**
 * Created by catub on 8/4/2017.
 */
public class DateComparator implements Comparator<ScratcherGame> {
    @Override
    public int compare(ScratcherGame o1, ScratcherGame o2) {
        return (o2.getStartDate().compareTo(o1.getStartDate()));
    }
}