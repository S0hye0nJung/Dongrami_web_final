package com.lec.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.lec.dto.WebReadingDTO;
import com.lec.entity.Member;
import com.lec.entity.WebReading;

@Repository
public interface ResultRepository extends JpaRepository<WebReading, Integer> {
	
	
	 @Query(nativeQuery = true, value = """
				WITH RECURSIVE RandomReadings AS (
			    (SELECT 
			        'reading1' AS type,
			        web_reading_id,
			        reading1 AS reading,
			        reading1_title AS title,
			        card_id
			    FROM web_reading
			    WHERE subcategory_id = :subcategoryId AND reading1 IS NOT NULL AND LENGTH(TRIM(reading1)) > 0
			    ORDER BY RAND()
			    LIMIT 1)
			
			    UNION ALL
			
			    (SELECT 
			        'reading2' AS type,
			        web_reading_id,
			        reading2 AS reading,
			        reading2_title AS title,
			        card_id
			    FROM web_reading
			    WHERE subcategory_id = :subcategoryId AND reading2 IS NOT NULL AND LENGTH(TRIM(reading2)) > 0
			    ORDER BY RAND()
			    LIMIT 1)
			
			    UNION ALL
			
			    (SELECT 
			        'reading3' AS type,
			        web_reading_id,
			        reading3 AS reading,
			        reading3_title AS title,
			        card_id
			    FROM web_reading
			    WHERE subcategory_id = :subcategoryId AND reading3 IS NOT NULL AND LENGTH(TRIM(reading3)) > 0
			    ORDER BY RAND()
			    LIMIT 1)
			)
			SELECT 
			    r.type,
			    r.web_reading_id AS webReadingId,
			    r.reading,
			    r.title AS readingTitle,
			    c.card_id AS cardId,
			    c.image_url AS cardImageUrl
			FROM RandomReadings r
			JOIN cards c ON r.card_id = c.card_id
				            """)
	    List<WebReadingProjection> findRandomReadingsForSubcategory(@Param("subcategoryId") int subcategoryId);

	    interface WebReadingProjection {
	        String getType();
	        int getWebReadingId();
	        String getReading();
	        String getReadingTitle();
	        int getCardId();
	        String getCardImageUrl();
	    }
	    
	    
    @Query(value = "SELECT w.web_reading_id AS webReadingId, " +
            "w.reading1, " +
            "w.reading1_title AS reading1Title, " +
            "w.subcategory_id AS subcategoryId, " +
            "w.card_id AS cardId, " +
            "c.image_url  AS imageUrl1 " +
            "FROM web_reading w, cards c "+
            "WHERE w.subcategory_id = :subcategoryId " +
            "and w.card_id= c.card_id "+
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
     List<Object[]> findOneCardReadings(@Param("subcategoryId") int subcategoryId);
	
	
	
	
	
//    @Query(value = "SELECT w.web_reading_id AS webReadingId, " +
//           "w.reading1, w.reading2, w.reading3, " +
//           "w.reading1_title AS reading1Title, " +
//           "w.reading2_title AS reading2Title, " +
//           "w.reading3_title AS reading3Title, " +
//           "w.subcategory_id AS subcategoryId, " +
//           "w.card_id AS cardId, " +
//           "(SELECT c1.image_url FROM cards c1 ORDER BY RAND() LIMIT 1) AS imageUrl1, " +
//           "(SELECT c2.image_url FROM cards c2 ORDER BY RAND() LIMIT 1) AS imageUrl2, " +
//           "(SELECT c3.image_url FROM cards c3 ORDER BY RAND() LIMIT 1) AS imageUrl3 " +
//           "FROM web_reading w " +
//           "WHERE w.subcategory_id = :subcategoryId " +
//           "ORDER BY RAND() " +
//           "LIMIT 1", nativeQuery = true)
//    List<Object[]> findRandomCardReadings(@Param("subcategoryId") int subcategoryId);
	
    
}


