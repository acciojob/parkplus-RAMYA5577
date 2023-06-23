package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum.
        // Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        Reservation reservation = new Reservation();

        if (userRepository3.findById(userId) == null) {
            throw new Exception("Cannot make reservation");
        }
        User user = userRepository3.findById(userId).get();

        if (parkingLotRepository3.findById(parkingLotId) == null) {
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        List<Spot> spotList = parkingLot.getSpotList();
        Spot optionalSpot = null;
        int optimalPrice = Integer.MAX_VALUE;
        for (Spot spot : spotList) {
            if (spot.getOccupied() == false) {
                if (spot.getSpotType().equals(SpotType.TWO_WHEELER))
                    if (numberOfWheels <= 2) {
                        if (optimalPrice > spot.getPricePerHour()) {
                            optimalPrice = spot.getPricePerHour();
                            optionalSpot = spot;
                        }
                    }
            }
            else if (spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                if (numberOfWheels <= 4) {
                    if (optimalPrice > spot.getPricePerHour()) {
                        optimalPrice = spot.getPricePerHour();
                        optionalSpot = spot;
                    }
                }
            }
            else {
                if (optimalPrice > spot.getPricePerHour()) {
                    optimalPrice = spot.getPricePerHour();
                    optionalSpot = spot;
                }
            }
        }
        if (optionalSpot==null){
            throw new Exception("Cannot make reservation");
        }
        optionalSpot.setOccupied(true);

        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(optionalSpot);

        List<Reservation> reservations=user.getReservationList();
        List<Reservation> reservationList=optionalSpot.getReservationList();

        reservations.add(reservation);
        reservationList.add(reservation);

        user.setReservationList(reservationList);
        optionalSpot.setReservationList(reservationList);
        userRepository3.save(user);
        spotRepository3.save(optionalSpot);
        return reservation;
    }
}
