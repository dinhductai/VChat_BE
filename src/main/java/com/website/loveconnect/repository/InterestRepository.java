package com.website.loveconnect.repository;

import com.website.loveconnect.entity.Interest;
import com.website.loveconnect.repository.query.InterestQueries;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
@Transactional
@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    // Get All Interest
    @Query(value = "SELECT i.* FROM interests i JOIN user_interests ui ON ui.interest_id = i.interest_id WHERE ui.user_id = :idUser", nativeQuery = true)
    List<Interest> getAllInterest(@Param("idUser") int idUser);

    List<Interest> getByInterestNameNotIn(List<String> interestNames);
    List<Interest> getByInterestNameIn(List<String> interestNames);

    @Query(value = InterestQueries.FIND_ALL_INTEREST_NAME,nativeQuery = true)
    List<String> findAllInterestName();

    Optional<Interest> findByInterestName(String name);

}
