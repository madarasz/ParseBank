package com.madarasz.parsebank.spring;

import com.madarasz.parsebank.Operations;
import com.madarasz.parsebank.database.Category;
import com.madarasz.parsebank.database.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by imadaras on 12/7/15.
 */
@Controller
public class CategoryControiler {

    private static final Logger logger = LoggerFactory.getLogger(CategoryControiler.class);

    @Autowired
    Neo4jOperations template;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    Operations operations;

    @RequestMapping(value="/Category", method = RequestMethod.POST)
    public String create(@RequestParam(value = "title") String title,
                         @RequestParam(value = "regex") String regex) {
        Category category = new Category(title, regex);
        categoryRepository.save(category);
        logger.info(String.format("Saved new Category: %s - %s", title, regex));
        return "redirect:/";
    }

    @RequestMapping(value="/Category/Delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable(value = "id") Long id) {
        categoryRepository.delete(categoryRepository.findById(id));
        return "redirect:/";
    }

    @RequestMapping(value="/Category/Recalculate", method = RequestMethod.POST)
    public String recalculate() {
        operations.assignCategories();
        return "redirect:/";
    }
}
