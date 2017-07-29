package org.launchcode.luckbasedmission.models.data;

import org.launchcode.luckbasedmission.models.ScratcherGameCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by catub on 7/26/2017.
 */
@Repository
@Transactional
public interface ScratcherGameCustomDao extends CrudRepository<ScratcherGameCustom, Integer> {
}
