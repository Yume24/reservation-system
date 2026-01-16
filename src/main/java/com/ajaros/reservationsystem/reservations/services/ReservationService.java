package com.ajaros.reservationsystem.reservations.services;

import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.entites.Reservation;
import com.ajaros.reservationsystem.reservations.exceptions.InvalidReservationException;
import com.ajaros.reservationsystem.reservations.exceptions.ReservationNotFoundException;
import com.ajaros.reservationsystem.reservations.mappers.ReservationMapper;
import com.ajaros.reservationsystem.reservations.repositories.ReservationRepository;
import com.ajaros.reservationsystem.rooms.entities.Room;
import com.ajaros.reservationsystem.users.entities.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final EntityManager entityManager;

  public List<ReservationResponse> getFilteredReservations(
      long userId, Instant from, Instant to, Long roomId) {
    return reservationRepository.findFiltered(from, to, roomId, userId).stream()
        .map(reservationMapper::toReservationResponse)
        .toList();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationResponse createReservation(Instant from, Instant to, Long roomId, Long userId) {
    checkRoomAvailability(from, to, roomId);

    var room = entityManager.getReference(Room.class, roomId);
    var user = entityManager.getReference(User.class, userId);

    var reservation = Reservation.builder().fromDate(from).toDate(to).user(user).room(room).build();

    return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
  }

  @Transactional
  public void deleteReservation(Long reservationId, long userId) {
    var reservation = findReservationById(reservationId);
    checkIfUserHasPermissionToModifyReservation(reservation, userId);

    reservationRepository.deleteById(reservationId);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationResponse updateReservation(
      Long reservationId, Long userId, Long roomId, Instant fromDate, Instant toDate) {
    var reservation = findReservationById(reservationId);

    checkIfUserHasPermissionToModifyReservation(reservation, userId);
    checkRoomAvailability(fromDate, toDate, roomId);

    reservation.setFromDate(fromDate);
    reservation.setToDate(toDate);
    reservation.setRoom(entityManager.getReference(Room.class, roomId));

    return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
  }

  private void checkRoomAvailability(Instant from, Instant to, Long roomId) {
    var reservations = reservationRepository.findFiltered(from, to, roomId, null);
    if (!reservations.isEmpty())
      throw new InvalidReservationException("Room is not available during the selected period");
  }

  private void checkIfUserHasPermissionToModifyReservation(Reservation reservation, Long userId) {
    if (!reservation.getUser().getId().equals(userId))
      throw new InvalidReservationException("Unauthorized");
  }

  private Reservation findReservationById(Long reservationId) {
    return reservationRepository
        .findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException(reservationId));
  }
}
