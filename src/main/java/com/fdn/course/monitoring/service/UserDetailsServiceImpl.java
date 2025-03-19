package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.dto.validation.ValRegistrationDTO;
import com.fdn.course.monitoring.model.Otp;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.OtpRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.BcryptImpl;
import com.fdn.course.monitoring.util.SendMailOTP;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

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

        int intOtp = 0;

        if (userOptional.isEmpty()) {
            user.setPassword(BcryptImpl.hash(user.getUsername() + user.getPassword()));
            intOtp = random.nextInt(111111, 999999);
            User userSaved = userRepository.save(user);
            Otp otp = new Otp();
            otp.setUser(userSaved);
            otp.setOtp(BcryptImpl.hash(String.valueOf(intOtp)));
            otp.setExpiredTime(LocalDateTime.now().plusMinutes(5));

            otpRepository.save(otp);
            map.put("message", "User registered successfully, OTP sent");
        }else {
            map.put("message", "Username already exists");
        }

        SendMailOTP.verifyRegisOTP(
                "VERIFIKASI OTP REGISTERASI",
                user.getNama(),
                user.getEmail(),
                String.valueOf(intOtp)
        );

        return ResponseEntity.ok(map);
    }


    public User convertDtoToEntity(ValRegistrationDTO regisDTO){
        return modelMapper.map(regisDTO, User.class);
    }
}
