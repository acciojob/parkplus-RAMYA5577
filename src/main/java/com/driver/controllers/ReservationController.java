package com.driver.controllers;

import com.driver.model.Reservation;
import com.driver.services.ReservationService;
import com.driver.services.impl.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @PostMapping("/reserveSpot")
    public Reservation reserveSpot(@RequestParam Integer userId, @RequestParam Integer parkingLotId, @RequestParam Integer timeInHours, @RequestParam Integer numberOfWheels) throws Exception{

        Reservation reservation;
        try{
            reservation=reservationService.reserveSpot(userId,parkingLotId,timeInHours,numberOfWheels);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return reservation;
    }
}
