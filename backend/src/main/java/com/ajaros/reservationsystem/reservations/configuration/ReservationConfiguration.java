package com.ajaros.reservationsystem.reservations.configuration;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "reservation")
@Configuration
@Data
public class ReservationConfiguration {
  private Duration maxDuration;
}
