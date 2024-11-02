package com.kim.customer.repository;

import com.kim.customer.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByMobileNumber(String mobileNumber);

    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR c.mobileNumber LIKE CONCAT('%', :searchTerm, '%')")
    List<Customer> findByNameOrPhone(@Param("searchTerm") String searchTerm);


}
