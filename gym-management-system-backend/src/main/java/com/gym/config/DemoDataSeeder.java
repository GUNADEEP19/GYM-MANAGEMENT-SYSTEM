package com.gym.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gym.model.AppUser;
import com.gym.model.Attendance;
import com.gym.model.AttendanceStatus;
import com.gym.model.Exercise;
import com.gym.model.GymPackage;
import com.gym.model.Member;
import com.gym.model.MemberStatus;
import com.gym.model.Membership;
import com.gym.model.MembershipStatus;
import com.gym.model.Payment;
import com.gym.model.PaymentMethod;
import com.gym.model.PaymentStatus;
import com.gym.model.ProgressRecord;
import com.gym.model.UserRole;
import com.gym.model.WorkoutPlan;
import com.gym.repository.AppUserRepository;
import com.gym.repository.AttendanceRepository;
import com.gym.repository.ExerciseRepository;
import com.gym.repository.GymPackageRepository;
import com.gym.repository.MemberRepository;
import com.gym.repository.MembershipRepository;
import com.gym.repository.PaymentRepository;
import com.gym.repository.ProgressRecordRepository;
import com.gym.repository.WorkoutPlanRepository;

/**
 * Initializes a massive set of realistic default data specifically for Demo/Viva
 * evaluations. Generates Trainers, Members, Workout Plans, Payments, and Activity Logs.
 */
@Component
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private final Random random = new Random();

    private final AppUserRepository userRepository;
    private final MemberRepository memberRepository;
    private final GymPackageRepository packageRepository;
    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final AttendanceRepository attendanceRepository;
    private final ProgressRecordRepository progressRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(AppUserRepository userRepository, MemberRepository memberRepository,
            GymPackageRepository packageRepository, MembershipRepository membershipRepository,
            PaymentRepository paymentRepository, WorkoutPlanRepository workoutPlanRepository,
            ExerciseRepository exerciseRepository, AttendanceRepository attendanceRepository,
            ProgressRecordRepository progressRecordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.packageRepository = packageRepository;
        this.membershipRepository = membershipRepository;
        this.paymentRepository = paymentRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
        this.attendanceRepository = attendanceRepository;
        this.progressRecordRepository = progressRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Prevent double seeding
        if (userRepository.count() > 2) {
            log.info("Database already contains demo user data. Skipping extensive seeding.");
            return;
        }

        log.info("Injecting massive default Demo Database state...");
        
        String defaultPasswordHash = passwordEncoder.encode("password123");

        // 1. Seed Packages
        GymPackage basicPkg = new GymPackage();
        basicPkg.setName("1-Month Basic Starter");
        basicPkg.setPrice(49.99);
        basicPkg.setDurationMonths(1);
        packageRepository.save(basicPkg);

        GymPackage premiumPkg = new GymPackage();
        premiumPkg.setName("12-Month Premium Elite");
        premiumPkg.setPrice(499.99);
        premiumPkg.setDurationMonths(12);
        packageRepository.save(premiumPkg);

        // 2. Seed Trainers (AppUser entity)
        AppUser trainer1 = AppUser.builder().name("David Goggins").email("trainer1@gym.com").phone("1231231231")
            .role(UserRole.TRAINER).passwordHash(defaultPasswordHash).build();
        AppUser trainer2 = AppUser.builder().name("Chris Bumstead").email("trainer2@gym.com").phone("2342342342")
            .role(UserRole.TRAINER).passwordHash(defaultPasswordHash).build();
        AppUser trainer3 = AppUser.builder().name("Arnold S.").email("trainer3@gym.com").phone("3453453453")
            .role(UserRole.TRAINER).passwordHash(defaultPasswordHash).build();
        
        trainer1 = userRepository.save(trainer1);
        trainer2 = userRepository.save(trainer2);
        trainer3 = userRepository.save(trainer3);

        List<AppUser> trainers = List.of(trainer1, trainer2, trainer3);

        // 3. Seed Members and Related Data
        for (int i = 1; i <= 3; i++) {
            AppUser assignedTrainer = trainers.get(i - 1); // 1 trainer per member
            
            // a. Create Member Profile
            Member memberProfile = Member.builder()
                .name("Demo Member " + i)
                .email("member" + i + "@gym.com")
                .phone("999888770" + i)
                .status(MemberStatus.ACTIVE)
                .joinDate(LocalDate.now().minusMonths(i))
                .trainerUserId(assignedTrainer.getId())
                .build();
            memberProfile = memberRepository.save(memberProfile);

            // b. Create User Profile
            AppUser userAcct = AppUser.builder()
                .name("Demo Member " + i)
                .email("member" + i + "@gym.com")
                .phone("999888770" + i)
                .role(UserRole.MEMBER)
                .passwordHash(defaultPasswordHash)
                .memberId(memberProfile.getId()) // Cross link
                .build();
            userRepository.save(userAcct);

            // c. Assign Membership
            Membership ms = new Membership();
            ms.setMember(memberProfile);
            ms.setGymPackage(i % 2 == 0 ? premiumPkg : basicPkg);
            ms.setStartDate(LocalDate.now().minusDays(10));
            ms.setEndDate(ms.getStartDate().plusMonths(ms.getGymPackage().getDurationMonths()));
            ms.setStatus(MembershipStatus.ACTIVE);
            membershipRepository.save(ms);

            // d. Assign Payment History
            Payment pay = new Payment();
            pay.setMember(memberProfile);
            pay.setAmount(ms.getGymPackage().getPrice());
            pay.setMethod(PaymentMethod.CARD);
            pay.setStatus(PaymentStatus.SUCCESS);
            pay.setPaidAt(LocalDateTime.now().minusDays(10));
            paymentRepository.save(pay);

            // e. Assign Workout Plan
            WorkoutPlan plan = WorkoutPlan.builder()
                .memberId(memberProfile.getId())
                .trainerUserId(assignedTrainer.getId())
                .planName((i == 1 ? "Weight Loss" : (i == 2 ? "Hypertrophy" : "Powerbuilding")) + " Plan")
                .description("Intensive routine prescribed by " + assignedTrainer.getName())
                .durationWeeks(8)
                .difficultyLevel("INTERMEDIATE")
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();
            plan = workoutPlanRepository.save(plan);

            // f. Assign Exercises
            String[] exerciseNames = {"Bench Press", "Squat", "Deadlift", "Pull-ups"};
            for (String exName : exerciseNames) {
                Exercise ex = new Exercise();
                ex.setPlan(plan);
                ex.setExerciseName(exName);
                ex.setSets(3 + random.nextInt(3)); // 3-5 sets
                ex.setReps(8 + random.nextInt(8)); // 8-15 reps
                ex.setBodyPart("Full Body");
                ex.setInstructions("Keep core tight. Hydrate between sets.");
                exerciseRepository.save(ex);
            }

            // g. Assign Past Attendance Logs
            for (int d = 1; d <= 5; d++) {
                Attendance att = new Attendance();
                att.setMember(memberProfile);
                att.setAttendanceDate(LocalDate.now().minusDays(d));
                att.setCheckInTime(LocalDateTime.now().minusDays(d).minusHours(2));
                att.setCheckOutTime(LocalDateTime.now().minusDays(d));
                att.setStatus(AttendanceStatus.CHECKED_OUT);
                attendanceRepository.save(att);
            }

            // h. Assign Progress Records (simulating weight loss/gain tracking)
            double baseWeight = 80.0 + random.nextInt(20);
            for (int p = 1; p <= 3; p++) {
                ProgressRecord pr = new ProgressRecord();
                pr.setMemberId(memberProfile.getId());
                pr.setPlan(plan);
                pr.setWeekNumber(p);
                pr.setExercisesDone(3 * p);
                pr.setWeight(baseWeight - p);
                pr.setBmi(pr.getWeight() / (1.8 * 1.8));
                pr.setProgressNotes("Feeling stronger. Diet adherence is good.");
                pr.setCreatedAt(LocalDateTime.now().minusWeeks(4 - p));
                progressRecordRepository.save(pr);
            }
        }
        
        log.info("SUCCESS: Massive Demo Data state injected flawlessly!");
    }
}
