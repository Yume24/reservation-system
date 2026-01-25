package com.ajaros.reservationsystem.reservations.services;

import com.ajaros.reservationsystem.reservations.configuration.ReservationConfiguration;
import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.entities.Reservation;
import com.ajaros.reservationsystem.reservations.exceptions.InvalidReservationException;
import com.ajaros.reservationsystem.reservations.exceptions.ReservationNotFoundException;
import com.ajaros.reservationsystem.reservations.mappers.ReservationMapper;
import com.ajaros.reservationsystem.reservations.repositories.ReservationRepository;
import com.ajaros.reservationsystem.reservations.repositories.ReservationSpecifications;
import com.ajaros.reservationsystem.rooms.services.RoomService;
import com.ajaros.reservationsystem.users.services.UserService;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationService {

  private final ReservationConfiguration reservationConfiguration;
  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final RoomService roomService;
  private final UserService userService;

  public List<ReservationResponse> getFilteredReservations(
      Long userId, Instant from, Instant to, Long roomId) {
    if (roomId != null) {
      roomService.roomExists(roomId);
    }
    var spec = ReservationSpecifications.filtered(from, to, roomId, userId, null);
    return reservationRepository.findAll(spec).stream()
        .map(reservationMapper::toReservationResponse)
        .toList();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationResponse createReservation(Instant from, Instant to, Long roomId, Long userId) {
    validateReservationDuration(from, to);
    validateRoomAvailability(from, to, roomId, null);

    var room = roomService.getRoomById(roomId);
    var user = userService.getUserById(userId);

    var reservation = Reservation.builder().fromDate(from).toDate(to).user(user).room(room).build();

    return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
  }

  @Transactional
  public void deleteReservation(Long reservationId, Long userId) {
    var reservation = findReservationById(reservationId);
    validateOwnership(reservation, userId);
    reservationRepository.deleteById(reservationId);
  }

  public void deleteReservation(Long reservationId) {
    reservationRepository.deleteById(reservationId);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationResponse updateReservation(
      Long reservationId, Long userId, Long roomId, Instant fromDate, Instant toDate) {
    var reservation = findReservationById(reservationId);
    validateOwnership(reservation, userId);
    return performUpdate(reservation, roomId, fromDate, toDate);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationResponse updateReservation(
      Long reservationId, Long roomId, Instant fromDate, Instant toDate) {
    var reservation = findReservationById(reservationId);
    return performUpdate(reservation, roomId, fromDate, toDate);
  }

  private ReservationResponse performUpdate(
      Reservation reservation, Long roomId, Instant from, Instant to) {
    validateReservationDuration(from, to);
    validateRoomAvailability(from, to, roomId, reservation.getId());

    reservation.setFromDate(from);
    reservation.setToDate(to);
    reservation.setRoom(roomService.getRoomById(roomId));

    return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
  }

  private void validateRoomAvailability(
      Instant from, Instant to, Long roomId, Long reservationIdToExclude) {
    if (reservationRepository.existsConflicting(roomId, from, to, reservationIdToExclude)) {
      throw new InvalidReservationException("Room is not available during the selected period");
    }
  }

  private void validateOwnership(Reservation reservation, Long userId) {
    if (!reservation.getUser().getId().equals(userId)) {
      throw new InvalidReservationException("Unauthorized");
    }
  }

  private Reservation findReservationById(Long reservationId) {
    return reservationRepository
        .findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException(reservationId));
  }

  private void validateReservationDuration(Instant from, Instant to) {
    if (from.until(to).compareTo(reservationConfiguration.getMaxDuration()) > 0) {
      throw new InvalidReservationException("Reservation duration exceeds the maximum allowed");
    }
  }
}
