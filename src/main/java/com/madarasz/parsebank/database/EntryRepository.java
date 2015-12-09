package com.madarasz.parsebank.database;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by imadaras on 12/6/15.
 */
public interface EntryRepository extends GraphRepository<Entry>, RelationshipOperationsRepository<Entry> {

    Entry findById(Long id);

    @Query("MATCH (e:Entry {serial: {0}, money: {1}, date: {2}}) RETURN e LIMIT 1")
    Entry getEntry(int serial, int money, Date date);

    @Query("MATCH (e:Entry) RETURN e")
    List<Entry> getAll();

    @Query("MATCH (e:Entry {id: {0}})-->(:Category) RETURN e LIMIT 1")
    Entry isInCategory(long id);

    @Query("MATCH (e:Entry) WHERE NOT (e:Entry)-->(:Category) RETURN COUNT(DISTINCT e)")
    int countNotInCategory();

    @Query("MATCH (e:Entry) WHERE NOT (e:Entry)-->(:Category) RETURN e ORDER BY e.date ASC")
    List<Entry> getUncategorized();

    @Query("MATCH (e:Entry)-[:FORCED]->(:Category) RETURN e ORDER BY e.date ASC")
    List<Entry> getForced();

    @Query("MATCH (e:Entry)-[:FORCED]->(:Category) RETURN COUNT(DISTINCT e)")
    int countForced();

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} RETURN e ORDER BY e.date ASC")
    List<Entry> getBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} AND NOT (e:Entry)-->(:Category) RETURN e ORDER BY e.date ASC")
    List<Entry> getUncategorizedBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry)-->(:Category {title: {2}}) WHERE e.date >= {0} AND e.date < {1} RETURN DISTINCT(e) ORDER BY e.date ASC")
    List<Entry> getCategoryBetweenDates(Date begin, Date end, String categoryTitle);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} RETURN SUM(DISTINCT(e).money)")
    int sumMoneyBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry)-->(:Category {title: {2}}) WHERE e.date >= {0} AND e.date < {1} RETURN SUM(DISTINCT(e).money)")
    int sumCategoryMoneyBetweenDates(Date begin, Date end, String title);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} " +
            "AND NOT (e:Entry)-->(:Category) RETURN SUM(e.money)")
    int sumNoCategoryMoneyBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry) RETURN MAX(e.date)")
    Date getMaxDate();

    @Query("MATCH (e:Entry) RETURN MIN(e.date)")
    Date getMinDate();

    @Query("MATCH (e:Entry)-->(:Category {title: {0}}) RETURN DISTINCT(e) ORDER BY e.date ASC")
    List<Entry> getCategory(String categoryTitle);

    @Query("MATCH (e:Entry)-->(:Category {title: {0}}) RETURN COUNT(DISTINCT e)")
    int countCategory(String categoryTitle);

    @Query("MATCH (e:Entry {code: 'cash'}) RETURN e ORDER BY e.date ASC")
    List<Entry> getCash();

    @Query("MATCH (e:Entry {code: 'cash'}) RETURN COUNT(DISTINCT e)")
    int countCash();

    @Query("MATCH (e:Entry) RETURN SUM(e.money)")
    int getSum();
}
