package org.launchcode.luckbasedmission.models.data;

import org.launchcode.luckbasedmission.models.ScratcherGameOverview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by catub on 7/26/2017.
 */
@Repository
@Transactional
public interface ScratcherGameOverviewDao extends CrudRepository<ScratcherGameOverview, Integer> {
}
