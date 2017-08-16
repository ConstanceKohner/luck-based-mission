package org.launchcode.luckbasedmission.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.launchcode.luckbasedmission.models.ScratcherGame;
import org.launchcode.luckbasedmission.models.ScratcherGameCustom;
import org.launchcode.luckbasedmission.models.ScratcherGameOverview;
import org.launchcode.luckbasedmission.models.ScratcherGameSnapshot;
import org.launchcode.luckbasedmission.models.comparators.*;
import org.launchcode.luckbasedmission.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

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

    //display index, sorted per request params
    @RequestMapping(value = "")
    public String index (Model model, @RequestParam(name="sortfield", defaultValue = "date") String sortfield,
                         @RequestParam(name="sortdir", defaultValue = "desc") String sortdir) {
        //
        Iterable<ScratcherGameSnapshot> literallyAllGames = scratcherGameSnapshotDao.findAll();
        HashMap<Integer, ScratcherGameSnapshot> mapAllGames = new HashMap<>();
        ScratcherGameSnapshot targetGame;
        for (ScratcherGameSnapshot game : literallyAllGames) {
            //iterate through all games
            if (mapAllGames.containsKey(game.getGameID())) {
                //if the game in the HashMap is from an earlier day than (or the same day as) the game from the iterable
                //then add the new game, replacing the old game
                targetGame = mapAllGames.get(game.getGameID());
                if (targetGame.getCreatedDate().compareTo(game.getCreatedDate()) <= 0) {
                    mapAllGames.put(game.getGameID(), game);
                }
            } else {
                //if no game with this ID exists in the HashMap, put it in the HashMap
                mapAllGames.put(game.getGameID(), game);
            }
        }
        //put all games from the map into a list so they can be sorted
        ArrayList<ScratcherGameSnapshot> allGames = new ArrayList<>();
        allGames.addAll(mapAllGames.values());

        //sort based on request params
        //nested in case sortfield is specified but sortdir is not (or is set incorrectly); should not be possible if clicking app links but possible with user error
        if (sortfield.equals("date")) {
            if (sortdir.equals("desc")) {
                DateComparator comparator = new DateComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "dateindex";
            } else {
                DateComparator comparator = new DateComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        } else if (sortfield.equals("name")) {
            if (sortdir.equals("asc")) {
                NameComparator comparator = new NameComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "nameindex";
            } else {
                NameComparator comparator = new NameComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        } else if (sortfield.equals("number")) {
            if (sortdir.equals("asc")) {
                NumberComparator comparator = new NumberComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "numberindex";
            } else {
                NumberComparator comparator = new NumberComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        } else if (sortfield.equals("price")) {
            if (sortdir.equals("asc")) {
                PriceComparator comparator = new PriceComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "priceindex";
            } else {
                PriceComparator comparator = new PriceComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        } else if (sortfield.equals("return")) {
            if (sortdir.equals("desc")) {
                ReturnComparator comparator = new ReturnComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "returnindex";
            } else {
                ReturnComparator comparator = new ReturnComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        } else if (sortfield.equals("percentage")) {
            if (sortdir.equals("desc")) {
                ReturnPercentageComparator comparator = new ReturnPercentageComparator();
                allGames.sort(comparator);
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "returnpercentageindex";
            } else {
                ReturnPercentageComparator comparator = new ReturnPercentageComparator();
                allGames.sort(Collections.reverseOrder(comparator));
                model.addAttribute("allgames", allGames);
                model.addAttribute("title", "All Scratcher Games");
                return "index";
            }
        }
        //if the request params don't match the options above, just use the default sort
        DateComparator comparator = new DateComparator();
        allGames.sort(comparator);
        model.addAttribute("allgames", allGames);
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

    //display specific view for each game
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String individualView (Model model, @PathVariable int id) {
        ScratcherGame scratcherGame = scratcherGameDao.findOne(id);
        model.addAttribute("game", scratcherGame);
        model.addAttribute("title", scratcherGame.getName());

        //convert allPrizes into display mode
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

    /*
    //put data in database manually
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
    */

    //load the database automatically
    @RequestMapping(value = "load", method = RequestMethod.GET)
    public String requestDailyLoad(Model model) {
        return "loaddatabase";
    }

    @RequestMapping(value = "load", method = RequestMethod.POST)
    public String processDailyLoad (Model model) throws IOException {
        List<Integer> currentGameList = Arrays.asList(771,935,21,23,48,54,63,71,73,78,92,93,94,97,98,99,
                101,102,104,107,109,111,113,114,116,117,119,120,122,126,127,134,135,136,137,138,139,141,142,
                143,144,145,146,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,170);
        for (Integer gameID : currentGameList) {
            //go to the specified url
            String url = "http://www.molottery.com/scratchers.do?method=singlegame&game=" + gameID.toString();
            //get the document
            Document doc = Jsoup.connect(url).get();
            //take the first half of the h1 element, which should be the game name, all caps
            String name = ((doc.select("h1").first()).text()).split(" - ")[0];
            //get all of the dd elements
            Elements allDD = doc.select("dd");
            List<String> listDD = allDD.eachText();
            //not resilient, relies on there being a space between $ and ticketPrice
            String dollarAmount = (listDD.get(0)).split(" ")[1];
            double ticketPrice = Double.parseDouble(dollarAmount);
            //gets date numbers
            String fullDate = (listDD.get(1)).split(" ")[0];
            //save them individually
            int startMonth = Integer.parseInt(fullDate.split("-")[1]);
            int startDay = Integer.parseInt(fullDate.split("-")[2]);
            int startYear = Integer.parseInt(fullDate.split("-")[0]);
            //get the current date and save those, too
            LocalDate today = LocalDate.now();
            int createdMonth = today.getMonth().getValue();
            int createdDay = today.getDayOfMonth();
            int createdYear = today.getYear();
            //first, take all the words before the comma in average chances.  then, take the third word, which should be the odds.
            String chance = (((doc.select("dd").last()).text()).split(",")[0]).split(" ")[2];
            //turn into double
            double averageWinLossChance = Double.parseDouble(chance);
            //get the odds table
            String[] table = ((doc.select("td > div > table").first()).text()).split(" ");
            StringBuffer CSV = new StringBuffer();
            //on the first pass, do not put a comma in front on the value. subsequently, add comma prefixes
            String prefix = "";
            for (int i = 0; i < table.length; i++) {
                //i > 5 removes the headings
                //i % 3 != 2; removes the last column, for Overview
                //i % 3 != 1; removes the middle column, for Snapshot
                if (i > 5 && i % 3 != 1) {
                    if (table[i].equalsIgnoreCase("TICKET")) {
                        CSV.append(prefix + ticketPrice);
                        prefix = ",";
                    } else {
                        //do not add dollar signs or commas
                        CSV.append(prefix + table[i].replaceAll("[$,]", ""));
                        prefix = ",";
                    }
                }
            }
            String allPrizes = CSV.toString();

            /*this is the overview version, to be used with i % 3 != 2
            ScratcherGameOverview scratcherGameOverview = new ScratcherGameOverview();
            scratcherGameOverview.setGameID(gameID);
            scratcherGameOverview.setName(name);
            scratcherGameOverview.setTicketPrice(ticketPrice);
            scratcherGameOverview.setStartMonth(startMonth);
            scratcherGameOverview.setStartDay(startDay);
            scratcherGameOverview.setStartYear(startYear);
            scratcherGameOverview.setCreatedMonth(createdMonth);
            scratcherGameOverview.setCreatedDay(createdDay);
            scratcherGameOverview.setCreatedYear(createdYear);
            scratcherGameOverview.setAverageWinLossChance(averageWinLossChance);
            scratcherGameOverview.setAllPrizes(allPrizes);
            scratcherGameOverview.recalculateOdds();
            scratcherGameDao.save(scratcherGameOverview);*/

            //this is the Snapshot version, to be used with i % 3 != 1

            ScratcherGameSnapshot scratcherGameSnapshot = new ScratcherGameSnapshot();

            Iterable <ScratcherGameOverview> overviews = scratcherGameOverviewDao.findAll();
            for (ScratcherGameOverview overview : overviews) {
                if (overview.getGameID() == gameID) {
                    scratcherGameSnapshot.setOverviewGame(overview);
                    break;
                }
            }
            scratcherGameSnapshot.setGameID(gameID);
            scratcherGameSnapshot.setName(name);
            scratcherGameSnapshot.setTicketPrice(ticketPrice);
            scratcherGameSnapshot.setStartMonth(startMonth);
            scratcherGameSnapshot.setStartDay(startDay);
            scratcherGameSnapshot.setStartYear(startYear);
            scratcherGameSnapshot.setCreatedMonth(createdMonth);
            scratcherGameSnapshot.setCreatedDay(createdDay);
            scratcherGameSnapshot.setCreatedYear(createdYear);
            scratcherGameSnapshot.setAverageWinLossChance(averageWinLossChance);
            scratcherGameSnapshot.setAllPrizes(allPrizes);
            scratcherGameSnapshot.recalculateOdds();
            scratcherGameDao.save(scratcherGameSnapshot);
        }
            return "redirect:";
    }
/*
    //TODO "simulate odds based on this game" takes user to a form for a CustomGame with starting values equal to the game they chose
    @RequestMapping(value="addCustom", method=RequestMethod.GET)
    public String displayCustomGameForm (Model model) {
        model.addAttribute("title", "Customize Scratcher Game");
        model.addAttribute(new ScratcherGameCustom());
        model.addAttribute("scratcherGame", scratcherGameDao.findAll());
        return "addcustomgame";
    }

    @RequestMapping(value="addCustom", method=RequestMethod.POST)
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
