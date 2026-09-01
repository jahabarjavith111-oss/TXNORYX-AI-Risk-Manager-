package com.txnoryx.backend.controller;

import com.txnoryx.backend.routing.PaymentRoute;
import com.txnoryx.backend.routing.PaymentRouteRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final PaymentRouteRepository repo;
    public RouteController(PaymentRouteRepository repo) { this.repo = repo; }
    @GetMapping public List<PaymentRoute> all() { return repo.findAll(); }
}
