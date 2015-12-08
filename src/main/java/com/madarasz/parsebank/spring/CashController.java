package com.madarasz.parsebank.spring;

import com.madarasz.parsebank.Operations;
import com.madarasz.parsebank.database.Category;
import com.madarasz.parsebank.database.CategoryRepository;
import com.madarasz.parsebank.database.Entry;
import com.madarasz.parsebank.database.EntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by imadaras on 12/7/15.
 */
@Controller
public class CashController {

    @Autowired
    Neo4jOperations template;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    Operations operations;

    private static final Logger logger = LoggerFactory.getLogger(CashController.class);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


    @RequestMapping(value="/Cash", method = RequestMethod.GET)
    public String getCPPage(Model model) {
        model.addAttribute("categories", categoryRepository.getAll());
        model.addAttribute("countCash", entryRepository.countCash());
        model.addAttribute("cashEntries", entryRepository.getCash());
        return "Cash";
    }

    @RequestMapping(value="/Entry/Cash/Delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable(value = "id") Long id) {
        entryRepository.delete(entryRepository.findById(id));
        return "redirect:/Cash";
    }

    @RequestMapping(value="/Entry/Cash/New", method = RequestMethod.POST)
    public String create(@RequestParam(value = "category") String category,
                         @RequestParam(value = "message") String message,
                         @RequestParam(value = "money") String money,
                         @RequestParam(value = "date") String date) {
        try {
            Category category1 = categoryRepository.findByTitle(category);
            int moneyint = Integer.valueOf(money);
            Entry entry = new Entry(-1, "cash", "", "", message, moneyint, dateFormat.parse(date));
            entry.setCategory(category1);
            entry.setForcedCategory(category1);
            entryRepository.save(entry);
            // counter entry
            Category felvet = categoryRepository.findByTitle("felvétel");
            Entry entryCounter = new Entry(-1, "cash", "", "", message, -moneyint, dateFormat.parse(date));
            entryCounter.setCategory(felvet);
            entryCounter.setForcedCategory(felvet);
            entryRepository.save(entryCounter);
        } catch (Exception ex) {
            logger.error("logged exception", ex);
        }
        return "redirect:/Cash";
    }

}
