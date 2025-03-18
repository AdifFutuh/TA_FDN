package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.model.Otp;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.OtpRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.BcryptImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    private final Random random = new Random();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            //error handling
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }

    public ResponseEntity<Object> regis(User user, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        Map<String, Object> map = new HashMap<>();


        if (userOptional.isEmpty()) {
            int intOtp = random.nextInt(111111, 999999);
            user.setPassword(BcryptImpl.hash(user.getUsername() + user.getPassword()));

            User userSaved = userRepository.save(user);

            Otp otp = new Otp();
            otp.setUser(userSaved);
            otp.setOtp(BcryptImpl.hash(String.valueOf(intOtp)));
            otp.setExpiredTime(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

            otpRepository.save(otp);
            map.put("message", "User registered successfully, OTP sent");
            return ResponseEntity.ok(map);
        }
        map.put("message", "Username already exists");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);

    }
}
