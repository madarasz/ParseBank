package com.madarasz.parsebank.spring;

import com.madarasz.parsebank.Operations;
import com.madarasz.parsebank.database.Entry;
import com.madarasz.parsebank.database.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by imadaras on 12/7/15.
 */
@Controller
public class StatController {
    @Autowired
    Operations operations;

    @Autowired
    EntryRepository entryRepository;

    @RequestMapping(value="/Stats", method = RequestMethod.GET)
    public String getCPPage(Model model) {
        model.addAttribute("monthlyStat", operations.getMonthlyStat());
        return "Stats";
    }

    @RequestMapping(value="/Entry/Category/{title}", method = RequestMethod.GET)
    public @ResponseBody List<Entry> getCategory(@PathVariable(value="title") String title) {
        return entryRepository.getCategory(title);
    }
}
