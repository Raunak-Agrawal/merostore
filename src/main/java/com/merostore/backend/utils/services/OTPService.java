package com.merostore.backend.utils.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.merostore.backend.exception.ApiValidationException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OTPService {

    private static final Integer EXPIRE_MINS = 4;
    @Autowired
    private RestTemplate restTemplate;
    private LoadingCache<String, Integer> otpCache;

    @Value("${sms_api_key}")
    private String smsKey;

    public OTPService() {
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public Integer generateOTP(String key) {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public Boolean sendOTP(String to, String keyToCache) {
        if (getOtp(keyToCache) != 0) {
            throw new ApiValidationException("Failed to send OTP");
        }
        Integer otp = generateOTP(keyToCache);

        String message = "Your OTP for merostore is: " + otp;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        OTPRequest otpRequest = OTPRequest.builder()
                .auth_token(smsKey)
                .to(to)
                .text(message)
                .build();
        HttpEntity<OTPRequest> entity = new HttpEntity<>(otpRequest, headers);
        try {
//            String response = restTemplate.postForObject(
//                    "https://sms.aakashsms.com/sms/v3/send/", entity, String.class);
            log.info("OTP sent successfully:{}", otp);
            return true;
        } catch (Exception e) {
            log.info("Error occured:{}", e);
            throw new ApiValidationException("Failed to send OTP");
        }
    }

    public Integer getOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

    public Boolean validateOTP(Integer otp, String keyCached) {
        Integer existingOTPInCache = getOtp(keyCached);
        if (existingOTPInCache == 0 || !existingOTPInCache.equals(otp)) {
            return false;
        }
        return true;
    }

    @Data
    @Builder
    public static class OTPRequest {
        String auth_token;
        String to;
        String text;
    }

}