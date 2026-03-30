package com.gym.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, String> {

    List<Attendance> findByMemberId(String memberId);

    List<Attendance> findByMemberIdAndAttendanceDateBetween(String memberId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.member.userId = :memberId AND a.attendanceDate = :date")
    List<Attendance> findAttendanceByMemberAndDate(@Param("memberId") String memberId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.member.userId = :memberId AND a.status = 'CHECKED_IN' ORDER BY a.checkInTime DESC")
    List<Attendance> findCheckedInAttendance(@Param("memberId") String memberId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.member.userId = :memberId AND a.status = 'CHECKED_OUT' AND a.attendanceDate BETWEEN :startDate AND :endDate")
    Integer countAttendanceInDateRange(@Param("memberId") String memberId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
