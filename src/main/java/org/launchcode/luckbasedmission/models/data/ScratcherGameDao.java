package org.launchcode.luckbasedmission.models.data;

import org.launchcode.luckbasedmission.models.ScratcherGame;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by catub on 7/24/2017.
 */
@Repository
@Transactional
public interface ScratcherGameDao extends CrudRepository<ScratcherGame, Integer> {
}