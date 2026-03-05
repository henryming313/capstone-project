package com.centria.cabbookingmvp;

import com.centria.cabbookingmvp.entity.*;
import com.centria.cabbookingmvp.repository.CabRepository;
import com.centria.cabbookingmvp.repository.TripBookingRepository;
import com.centria.cabbookingmvp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DevDataRunner {

    @Bean
    CommandLineRunner seed(UserRepository userRepo, CabRepository cabRepo, TripBookingRepository tripRepo) {
        return args -> {
            if (userRepo.count() > 0 || cabRepo.count() > 0 || tripRepo.count() > 0) {
                return;
            }
            // 1) user
            User rider = new User();
            rider.setName("Test Rider");
            rider.setEmail("rider@test.com");
            rider.setPhone("0400000001");
            rider.setPasswordHash("demo_hash");
            rider.setStatus(UserStatus.ACTIVE);

// 关键：必须用 save 的返回值（此时 rider 已有 id）
            rider = userRepo.save(rider);

// 2) cab
            Cab cab = new Cab();
            cab.setPlateNumber("ABC-123");
            cab.setBrand("Toyota");
            cab.setModel("Prius");
            cab.setColor("White");
            cab.setCabType(CabType.SEDAN);
            cab = cabRepo.save(cab);

// 3) trip（再引用已保存的 rider/cab）
            TripBooking trip = new TripBooking();
            trip.setRider(rider);      // rider 已经是持久化对象（有 id）
            trip.setCab(cab);          // cab 也已保存
            trip.setPickupLocation("Helsinki Central Station");
            trip.setDropoffLocation("Centria Campus");
            trip.setStatus(TripStatus.REQUESTED);
            trip.setTotalFare(new BigDecimal("18.50"));

            tripRepo.save(trip);

            System.out.println("Seed done. users=" + userRepo.count()
                    + ", cabs=" + cabRepo.count()
                    + ", trips=" + tripRepo.count());
        };
    }
}