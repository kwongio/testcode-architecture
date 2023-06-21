package com.example.demo.common.infrastrucure;

import com.example.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {
    @Override
    public long millis() {
        return System.currentTimeMillis();
    }
}
