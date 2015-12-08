package com.madarasz.parsebank;

import au.com.bytecode.opencsv.CSVReader;
import com.madarasz.parsebank.database.Category;
import com.madarasz.parsebank.database.CategoryRepository;
import com.madarasz.parsebank.database.Entry;
import com.madarasz.parsebank.database.EntryRepository;
import com.madarasz.parsebank.database.stats.MonthlyStat;
import com.madarasz.parsebank.database.stats.StatRow;
import com.madarasz.parsebank.database.stats.SubCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by imadaras on 12/7/15.
 */
@Component
public class Operations {

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static final DateFormat dateFormatSHort = new SimpleDateFormat("yyyy.MMM");

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public void parseCSV(String path) throws IOException, ParseException {
        CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(path), Charset.forName("ISO-8859-1")), ';');
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            int money = Integer.valueOf(nextLine[8].split(",")[0]);
            if (nextLine[7].equals("-")) {
                money = -money;
            }
            int serial = Integer.valueOf(nextLine[2]);
            Date date = dateFormat.parse(nextLine[10]);
            Entry exists = entryRepository.getEntry(serial, money, date);
            if (exists == null) {
                Entry row = new Entry(serial, nextLine[3], nextLine[4], nextLine[5], nextLine[6], money, date);
                entryRepository.save(row);
                logger.info("New entry: " + row.toString());
            } else {
                logger.info("Already exists.");
            }
        }
    }

    public void resetCategories() {
        List<Entry> entries = entryRepository.getAll();
        for (Entry entry : entries) {
            entry.setCategory(null);
            entryRepository.save(entry);
        }
    }

    public void assignCategories() {
        resetCategories();
        List<Entry> entries = entryRepository.getAll();
        List<Category> categories = categoryRepository.getAll();

        for (Category category : categories) {
            Pattern pattern = Pattern.compile(category.getRegex());
            int count = 0;
            for (Entry entry : entries) {
                Matcher matcher1 = pattern.matcher(entry.getCode());
                Matcher matcher2 = pattern.matcher(entry.getMessage());
                Matcher matcher3 = pattern.matcher(entry.getRecipientName());
                boolean match1 = matcher1.find();
                boolean match2 = matcher2.find();
                boolean match3 = matcher3.find();
                if ((match1) ||
                        ((match2) && (!entry.getMessage().equals(""))) ||
                        (match3)) {
                    Entry already = entryRepository.isInCategory(entry.getId());
                    if (already != null) {
                        logger.warn(String.format("Cannot put into category: %s (%s) - already in category %s (%s)", category.getTitle(), category.getRegex(), already.getCategory().getTitle(), already.getCategory().getRegex()));
                        logger.warn("Entry already in a category: " + already.toString());
                    } else {
                        logger.trace(String.format("Putting into category %s (%s): %s", category.getTitle(), category.getRegex(), entry.toString()));
                        entry.setCategory(category);
                        entryRepository.save(entry);
                        count++;
                    }
                }
            }
            category.setCount(count);
            categoryRepository.save(category);
        }
    }

    public MonthlyStat getMonthlyStat() {
        LocalDate minDate = entryRepository.getMinDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        LocalDate maxDate = entryRepository.getMaxDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Category> categories = categoryRepository.getAll();
        List<String> categoryTitles = new ArrayList<>();
        for (Category category : categories) {
            categoryTitles.add(category.getTitle());
        }
        MonthlyStat result = new MonthlyStat(categoryTitles);

        for (LocalDate date = minDate; date.isBefore(maxDate); date = date.plusMonths(1)) {
            Date fromDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date toDate = Date.from(date.plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            List<SubCategory> subcategory = new ArrayList<>();
            for (Category category : categories) {
                subcategory.add(new SubCategory(entryRepository.sumCategoryMoneyBetweenDates(fromDate, toDate, category.getTitle()), category.getTitle()));
            }
            StatRow row = new StatRow(dateFormatSHort.format(fromDate),
                    entryRepository.sumMoneyBetweenDates(fromDate, toDate), subcategory, 0,
                    entryRepository.sumNoCategoryMoneyBetweenDates(fromDate, toDate));
            result.addRow(row);
        }

        return result;
    }
}
