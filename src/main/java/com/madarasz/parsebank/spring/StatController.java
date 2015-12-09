package com.madarasz.parsebank.spring;

import com.madarasz.parsebank.Operations;
import com.madarasz.parsebank.database.Entry;
import com.madarasz.parsebank.database.EntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by imadaras on 12/7/15.
 */
@Controller
public class StatController {

    private static final Logger logger = LoggerFactory.getLogger(StatController.class);
    private static final DateFormat df = new SimpleDateFormat("yyyy.MMM.dd");

    @Autowired
    Operations operations;

    @Autowired
    EntryRepository entryRepository;

    @RequestMapping(value="/Stats", method = RequestMethod.GET)
    public String getCPPage(Model model) {
        model.addAttribute("monthlyStat", operations.getMonthlyStat());
        model.addAttribute("sum", entryRepository.getSum());
        return "Stats";
    }

    @RequestMapping(value="/Entry/Category/{title}", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getCategory(@PathVariable(value="title") String title) {
        return entryRepository.getCategory(title);
    }

    @RequestMapping(value="/Entry/Month/{year}.{month}", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getMonth(@PathVariable(value="year") String year,
                                              @PathVariable(value="month") String month) {
        return entryRepository.getBetweenDates(datefromString(year, month), datePlusMonthfromString(year, month));
    }

    @RequestMapping(value="/Entry/Filter/{title}/{year}.{month}", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getCategoryMonth(
            @PathVariable(value="title") String title,
            @PathVariable(value="year") String year,
            @PathVariable(value="month") String month) {
        return entryRepository.getCategoryBetweenDates(datefromString(year, month), datePlusMonthfromString(year, month), title);
    }

    @RequestMapping(value="/Entry/Uncategorized", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getUncategorized() {
        return entryRepository.getUncategorized();
    }

    @RequestMapping(value="/Entry/Uncategorized/{year}.{month}", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getUncategorizedMonth(
            @PathVariable(value="year") String year,
            @PathVariable(value="month") String month) {
        return entryRepository.getUncategorizedBetweenDates(datefromString(year, month), datePlusMonthfromString(year, month));
    }

    private Date datefromString(String year, String month) {
        try {
            return df.parse(year + "." + month + ".1");
        } catch (ParseException e) {
            logger.error("logged parse exception", e);
            return new Date();
        }
    }

    private Date datePlusMonthfromString(String year, String month) {
        Date fromDate = datefromString(year, month);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(fromDate);
        toCalendar.add(Calendar.MONTH, 1);
        return  toCalendar.getTime();
    }
}
