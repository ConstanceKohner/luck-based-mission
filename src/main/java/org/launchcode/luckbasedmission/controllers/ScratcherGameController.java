package org.launchcode.luckbasedmission.controllers;

import org.launchcode.luckbasedmission.models.ScratcherGame;
import org.launchcode.luckbasedmission.models.ScratcherGameCustom;
import org.launchcode.luckbasedmission.models.ScratcherGameOverview;
import org.launchcode.luckbasedmission.models.ScratcherGameSnapshot;
import org.launchcode.luckbasedmission.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        Iterable<ScratcherGameSnapshot> literallyallgames = scratcherGameSnapshotDao.findAll();
        HashMap<Integer, ScratcherGameSnapshot> allgames = new HashMap<>();
        ScratcherGameSnapshot targetGame;
        for (ScratcherGameSnapshot game : literallyallgames) {
            if (allgames.containsKey(game.getGameID())) {
                targetGame = allgames.get(game.getGameID());
                //if the game in the HashMap is from an earlier day than (or the same day as) the game from the iterable, then add the new game
                if (targetGame.getCreatedDate().compareTo(game.getCreatedDate()) <= 0) {
                    allgames.put(game.getGameID(), game);
                }
            } else {
                //if no game with this ID exists in the hashmap, put it in the hashmap
                allgames.put(game.getGameID(), game);
            }
            //if neither of these is true, continue to the next loop
        }
        model.addAttribute("allgames", allgames.values());
        model.addAttribute("title", "All Scratcher Games");
        return "index";
    }

    @RequestMapping(value = "/overviews")
    public String allOverviews (Model model) {
        //to ensure same sort as index view, refactor once comparators come into play
        Iterable<ScratcherGameOverview> allOverviews = scratcherGameOverviewDao.findAll();
        HashMap<Integer, ScratcherGameOverview> allgames = new HashMap<>();
        for (ScratcherGameOverview game : allOverviews) {
            allgames.put(game.getGameID(), game);
        }
        model.addAttribute("allgames", allgames.values());
        model.addAttribute("title", "All Scratcher Game Overviews");
        return "index";
    }

    //TODO display specific view for each game
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String individualView (Model model, @PathVariable int id) {
        ScratcherGame scratcherGame = scratcherGameDao.findOne(id);
        model.addAttribute("game", scratcherGame);
        model.addAttribute("title", scratcherGame.getName());

        //convert allPrizes into display mode
        //TODO: control for potential length issues
        ArrayList<ArrayList<String>> displayMode = new ArrayList<>();
        String[] allPrizesArray = (scratcherGame.getAllPrizes()).split(",");
        for (int i = 0; i < (allPrizesArray.length); i = i+2) {
            //create the inner list
            ArrayList<String> innerList = new ArrayList<>();

            //change the dollar amount formatting
            NumberFormat dollars = NumberFormat.getCurrencyInstance();
            String key = dollars.format(Double.parseDouble(allPrizesArray[i]));

            //change the integer formatting
            NumberFormat integer = NumberFormat.getIntegerInstance();
            String value = integer.format(Double.parseDouble(allPrizesArray[i+1]));

            //add both to the inner list and add the inner list to the outer list
            innerList.add(key);
            innerList.add(value);
            displayMode.add(innerList);
        }
        model.addAttribute("displayMode", displayMode);

        return "individualview";
    }

    //put data in database
    @RequestMapping(value="addOverview", method=RequestMethod.GET)
    public String displayOverviewGameForm (Model model) {
        model.addAttribute("title", "Add a New Scratcher Overview");
        model.addAttribute(new ScratcherGameOverview());
        return "addoverviewgame";
    }

    @RequestMapping(value="addOverview", method=RequestMethod.POST)
    public String processOverviewGameForm (@ModelAttribute @Valid ScratcherGameOverview scratcherGameOverview, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a New Scratcher Overview");
            return "addoverviewgame";
        }
        //originally, recalculate odds was run internally, but this caused problems.  it is best to run it before saving each time.
        scratcherGameOverview.setCreatedDay(scratcherGameOverview.getStartDay());
        scratcherGameOverview.setCreatedMonth(scratcherGameOverview.getStartMonth());
        scratcherGameOverview.setCreatedYear(scratcherGameOverview.getStartYear());
        scratcherGameOverview.recalculateOdds();
        scratcherGameOverviewDao.save(scratcherGameOverview);
        return "redirect:overviews";
    }

    @RequestMapping(value="addSnapshot", method=RequestMethod.GET)
    public String displaySnapshotGameForm (Model model) {
        model.addAttribute("title", "Add a New Daily Snapshot");
        model.addAttribute("scratcherGameOverviews", scratcherGameOverviewDao.findAll());
        model.addAttribute(new ScratcherGameSnapshot());
        return "addsnapshotgame";
    }

    @RequestMapping(value = "addSnapshot", method = RequestMethod.POST)
    public String processSnapshotGameForm (@ModelAttribute @Valid ScratcherGameSnapshot scratcherGameSnapshot, Errors errors, @RequestParam int scratcherGameOverviewUidNumber, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a New Daily Snapshot");
            return "addsnapshotgame";
        }
        ScratcherGameOverview scratcherGame = scratcherGameOverviewDao.findOne(scratcherGameOverviewUidNumber);
        scratcherGameSnapshot.setOverviewGame(scratcherGame);
        scratcherGameSnapshot.setName(scratcherGame.getName());
        scratcherGameSnapshot.setGameID(scratcherGame.getGameID());
        scratcherGameSnapshot.setTicketPrice(scratcherGame.getTicketPrice());
        scratcherGameSnapshot.setStartMonth(scratcherGame.getStartMonth());
        scratcherGameSnapshot.setStartDay(scratcherGame.getStartDay());
        scratcherGameSnapshot.setStartYear(scratcherGame.getStartYear());
        scratcherGameSnapshot.setAverageWinLossChance(scratcherGame.getAverageWinLossChance());
        scratcherGameSnapshot.recalculateOdds();
        scratcherGameSnapshotDao.save(scratcherGameSnapshot);
        return "redirect:";
    }
/*
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
    */
}
