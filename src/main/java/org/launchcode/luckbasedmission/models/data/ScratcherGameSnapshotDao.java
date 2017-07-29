package org.launchcode.luckbasedmission.models.data;

import org.launchcode.luckbasedmission.models.ScratcherGameSnapshot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by catub on 7/26/2017.
 */
@Repository
@Transactional
public interface ScratcherGameSnapshotDao extends CrudRepository<ScratcherGameSnapshot, Integer> {
}
