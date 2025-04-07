package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.config.JwtConfig;
import com.fdn.course.monitoring.dto.response.MenuLoginDTO;
import com.fdn.course.monitoring.dto.validation.ValLoginDTO;
import com.fdn.course.monitoring.dto.validation.ValUserDTO;
import com.fdn.course.monitoring.dto.validation.ValVerifyRegisDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.Otp;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.OtpRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.BcryptImpl;
import com.fdn.course.monitoring.security.Crypto;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.SendMailOTP;
import com.fdn.course.monitoring.util.TransformationDataManual;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtility jwtUtility;

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

            SendMailOTP.verifyRegisOTP(
                    "VERIFIKASI OTP REGISTERASI",
                    user.getNama(),
                    user.getEmail(),
                    String.valueOf(intOtp)
            );
        }else {
            map.put("message", "Username already exists");
        }

        return ResponseEntity.ok(map);
    }

    public ResponseEntity<Object> verifyRegis(User user, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if (userOptional.isEmpty()){
            return GlobalResponse.dataTidakDitemukan("",request);
        }
        User userNext = userOptional.get();
        Otp otp = userNext.getOtp();

        if (otp == null) {
            return GlobalResponse.dataTidakValid("", request);
        }

        if (LocalDateTime.now().isAfter(otp.getExpiredTime())) {
            return GlobalResponse.dataTidakValid("", request);
        }

        if (!BcryptImpl.verifyHash(user.getOtp().getOtp(), otp.getOtp())) {
            return GlobalResponse.dataTidakValid("", request);
        }

        userNext.setIsRegistered(true);
        userRepository.save(userNext);

        return GlobalResponse.dataBerhasilDiubah(request);
    }

    public ResponseEntity<Object> resendOtp(String email, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return GlobalResponse.dataTidakDitemukan("", request);
        }

        User user = userOptional.get();
        Otp otp = user.getOtp();

        int newOtp = random.nextInt(111111, 999999);
        String hashedOtp = BcryptImpl.hash(String.valueOf(newOtp));

        if (otp == null) {
            otp = new Otp();
            otp.setUser(user);
        }

        otp.setOtp(hashedOtp);
        otp.setExpiredTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        SendMailOTP.verifyRegisOTP(
                "RESEND OTP VERIFIKASI",
                user.getNama(),
                user.getEmail(),
                String.valueOf(newOtp)
        );

        return GlobalResponse.dataBerhasilDiubah(request);
    }


    public ResponseEntity<Object> login(User user, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isEmpty()){
            return GlobalResponse.dataTidakDitemukan("",request);
        }
        User userNext = userOptional.get();

        if (!userNext.getIsRegistered()){
            return GlobalResponse.dataTidakDitemukan("",request);
        }

        if (!BcryptImpl.verifyHash(user.getUsername()+user.getPassword(), userNext.getPassword())){
            return GlobalResponse.passwordtidakValid("", request);
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userNext.getId());
        claims.put("phn", userNext.getNoHp());
        claims.put("nl", userNext.getNama());
        claims.put("ml", userNext.getEmail());

        String token = jwtUtility.doGenerateToken(claims,user.getUsername());
        if (JwtConfig.getTokenEncryptEnable().equals("y")){
            token = Crypto.performEncrypt(token);
        }
        List<MenuLoginDTO> ltMenu = modelMapper.map(userNext.getAkses().getLtMenu(), new TypeToken<List<MenuLoginDTO>>(){}.getType());

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        response.put("menu",new TransformationDataManual().doTransformAksesMenuLogin(ltMenu));

        return GlobalResponse.loginBerhasil(response, request);
    }



    public User convertDtoToEntity(ValUserDTO regisDTO){
        return modelMapper.map(regisDTO, User.class);
    }

    public User convertDtoToEntity(ValVerifyRegisDTO verifyRegisDTO){
        User user = new User();
        Otp otp = new Otp();
        otp.setOtp(verifyRegisDTO.getOtp());
        user.setEmail(verifyRegisDTO.getEmail());
        user.setOtp(otp);

        return user;
    }

    public User convertDtoToEntity(ValLoginDTO loginDTO){
        return modelMapper.map(loginDTO, User.class);
    }

}
