package org.launchcode.luckbasedmission.models.comparators;

import org.launchcode.luckbasedmission.models.ScratcherGame;

import java.util.Comparator;

/**
 * Created by catub on 8/13/2017.
 */
public class NameComparator implements Comparator<ScratcherGame>{
    @Override
    public int compare(ScratcherGame o1, ScratcherGame o2) {
        return o1.getName().compareTo(o2.getName());
    }
}