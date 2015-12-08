package com.madarasz.parsebank.spring;

import com.madarasz.parsebank.Operations;
import com.madarasz.parsebank.database.CategoryRepository;
import com.madarasz.parsebank.database.EntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.madarasz.parsebank.database.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by imadaras on 12/7/15.
 */
@Controller
public class BaseController {

    @Autowired
    Neo4jOperations template;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    Operations operations;

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


    @RequestMapping(value="/", method = RequestMethod.GET)
    public String getCPPage(Model model) {
        model.addAttribute("countEntries", template.count(Entry.class));
        model.addAttribute("categories", categoryRepository.getAll());
        model.addAttribute("uncategorizedEntries", entryRepository.getUncategorized());
        model.addAttribute("uncategorized", entryRepository.countNotInCategory());
        return "Index";
    }

    @RequestMapping(value="/ParseCSV", method = RequestMethod.POST)
    public String parseCSV(String path, final RedirectAttributes redirectAttributes) {
        try {
            long count = template.count(Entry.class);
            operations.parseCSV(path);
            count = template.count(Entry.class) - count;
            if (count > 0) {
                redirectAttributes.addFlashAttribute("successMessage",
                        String.format("%d new entries are added to DB", count));
            } else {
                redirectAttributes.addFlashAttribute("warningMessage",
                        String.format("No new entries are added to DB"));
            }
        } catch (Exception ex) {
            logger.error("logged exception", ex);
            redirectAttributes.addFlashAttribute("errorMessage",
                    String.format("Error loading CSV with path: %s", path));
        }
        return "redirect:/";
    }
}
