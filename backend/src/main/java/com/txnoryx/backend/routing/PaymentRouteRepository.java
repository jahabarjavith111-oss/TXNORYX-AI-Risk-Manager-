package com.txnoryx.backend.routing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRouteRepository extends JpaRepository<PaymentRoute, Long> {
    List<PaymentRoute> findByIsActiveTrue();
    java.util.Optional<PaymentRoute> findByRouteCode(String routeCode);
}
