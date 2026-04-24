package com.clean.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.clean.demo.entity.Order;

interface CleanerStatsProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPhone();
    Double getExperience();
    Double getRating();
    Long getOrderCount();
}


public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCleanersId(Long cleanerId);

    @Query("SELECT COUNT(o) FROM Order o")
    Integer sumTotalOrders();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.appointmentDate >= :startDate")
    Double sumOrdersFromLastMonth(@Param("startDate") LocalDateTime startDate);

    @Query("""
        SELECT YEAR(o.appointmentDate), MONTH(o.appointmentDate), COUNT(o)
        FROM Order o
        WHERE o.appointmentDate >= :startDate AND o.appointmentDate < :endDate
        GROUP BY YEAR(o.appointmentDate), MONTH(o.appointmentDate)
        ORDER BY YEAR(o.appointmentDate), MONTH(o.appointmentDate)
        """)
    List<Object[]> countOrdersGroupedByMonth(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = """
        SELECT 
            p.id as id, 
            p.first_name as firstName, 
            p.last_name as lastName, 
            p.email as email, 
            p.phone as phone, 
            c.experience as experience, 
            c.rating as rating, 
            COUNT(o.id) as orderCount
        FROM orders o
        JOIN orders_cleaners oc ON o.id = oc.order_id
        JOIN cleaner c ON oc.cleaners_id = c.id
        JOIN person p ON oc.cleaners_id = p.id
        GROUP BY p.id, p.first_name, p.last_name, p.email, p.phone, c.experience, c.rating
        ORDER BY orderCount DESC LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findOrdersCountByCleaner();
}
