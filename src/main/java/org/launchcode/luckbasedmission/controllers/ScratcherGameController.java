package org.launchcode.luckbasedmission.controllers;

import org.launchcode.luckbasedmission.models.ScratcherGame;
import org.launchcode.luckbasedmission.models.ScratcherGameCustom;
import org.launchcode.luckbasedmission.models.ScratcherGameOverview;
import org.launchcode.luckbasedmission.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by catub on 7/24/2017.
 */

@Controller
@RequestMapping(value = "")
public class ScratcherGameController {

    @Autowired
    private ScratcherGameDao scratcherGameDao;

    @Autowired
    private ScratcherGameOverviewDao scratcherGameOverviewDao;

    @Autowired
    private ScratcherGameSnapshotDao scratcherGameSnapshotDao;

    @Autowired
    private ScratcherGameCustomDao scratcherGameCustomDao;

    @RequestMapping(value = "")
    public String homePage (Model model) {
        model.addAttribute("alloverviewgames", scratcherGameOverviewDao.findAll());
        model.addAttribute("title", "All Scratcher Games");
        return "index";
    }

    //TODO display specific view for each game
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String individualView (Model model, @PathVariable int id) {
        ScratcherGame scratcherGame = scratcherGameDao.findOne(id);
        model.addAttribute("game", scratcherGame);
        model.addAttribute("title", scratcherGame.getName());
        return "individualview";
    }

    //delete later
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String placeholderFunction (Model model) {
        return "redirect: index";
    }

    //put data in database, this will be replaced later
    @RequestMapping(value="addoverview", method=RequestMethod.GET)
    public String displayOverviewGameForm (Model model) {
        model.addAttribute("title", "Add a New Scratcher Overview");
        model.addAttribute(new ScratcherGameOverview());
        return "addoverviewgame";
    }

    @RequestMapping(value="addoverview", method=RequestMethod.POST)
    public String processOverviewGameForm (@ModelAttribute @Valid ScratcherGameOverview scratcherGameOverview, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a New Scratcher Overview");
            return "addoverviewgame";
        }
        //originally, recalculate odds was run internally, but this caused problems.  it is best to run it before saving each time.
        scratcherGameOverview.recalculateOdds();
        scratcherGameOverviewDao.save(scratcherGameOverview);
        return "redirect:";
    }

    //TODO "simulate odds based on this game" takes user to a form for a CustomGame with starting values equal to the game they chose
    @RequestMapping(value="addcustom", method=RequestMethod.GET)
    public String displayCustomGameForm (Model model) {
        model.addAttribute("title", "Customize Scratcher Game");
        model.addAttribute(new ScratcherGameCustom());
        model.addAttribute("scratcherGame", scratcherGameDao.findAll());
        return "addcustomgame";
    }

    @RequestMapping(value="addcustom", method=RequestMethod.POST)
    //public String processCustomGameForm (@ModelAttribute @Valid ScratcherGameCustom scratcherGameCustom, Errors errors, @RequestParam int originalGameId, Model model) {
    public String processCustomGameForm (@ModelAttribute @Valid ScratcherGameCustom scratcherGameCustom, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Customize Scratcher Game");
            return "addcustomgame";
        }
        //ScratcherGame scratcherGame = scratcherGameDao.findOne(originalGameId);
        //scratcherGameCustom.setScratcherGame(scratcherGame);
        scratcherGameCustomDao.save(scratcherGameCustom);
        return "redirect:";
    }
}