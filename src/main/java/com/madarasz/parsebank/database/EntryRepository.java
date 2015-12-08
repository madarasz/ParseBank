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

    @Query("MATCH (e:Entry {serial: {0}, money: {1}, date: {2}}) RETURN e LIMIT 1")
    Entry getEntry(int serial, int money, Date date);

    @Query("MATCH (e:Entry) RETURN e")
    List<Entry> getAll();

    @Query("MATCH (e:Entry {serial: {0}, date: {1}})<-[:BELONGS]-(:Category) RETURN e LIMIT 1")
    Entry isInCategory(int id, Date date);

    @Query("MATCH (e:Entry) WHERE NOT (e:Entry)<-[:BELONGS]-(:Category) RETURN COUNT(DISTINCT e)")
    int countNotInCategory();

    @Query("MATCH (e:Entry) WHERE NOT (e:Entry)<-[:BELONGS]-(:Category) RETURN e ORDER BY e.date ASC")
    List<Entry> getUncategorized();

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} RETURN e")
    List<Entry> getBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} AND NOT (e:Entry)<-[:BELONGS]-(:Category) RETURN e")
    List<Entry> getUncategorizedBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry)<-[:BELONGS]-(:Category {title: {2}}) WHERE e.date >= {0} AND e.date < {1} RETURN e")
    List<Entry> getCategoryBetweenDates(Date begin, Date end, String categoryTitle);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} RETURN SUM(e.money)")
    int sumMoneyBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry)<-[:BELONGS]-(:Category {title: {2}}) WHERE e.date >= {0} AND e.date < {1} RETURN SUM(e.money)")
    int sumCategoryMoneyBetweenDates(Date begin, Date end, String title);

    @Query("MATCH (e:Entry) WHERE e.date >= {0} AND e.date < {1} " +
            "AND NOT (e:Entry)<-[:BELONGS]-(:Category) RETURN SUM(e.money)")
    int sumNoCategoryMoneyBetweenDates(Date begin, Date end);

    @Query("MATCH (e:Entry) RETURN MAX(e.date)")
    Date getMaxDate();

    @Query("MATCH (e:Entry) RETURN MIN(e.date)")
    Date getMinDate();

    @Query("MATCH (e:Entry)<-[:BELONGS]-(:Category {title: {0}}) RETURN e ORDER BY e.date ASC")
    List<Entry> getCategory(String categoryTitle);
}
