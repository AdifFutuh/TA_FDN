package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.response.RespUserDTO;
import com.fdn.course.monitoring.dto.validation.ValUserDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IService<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransformPagination transformPagination;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Deprecated
    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method ini tidak digunakan. Gunakan UserDetailsServiceImpl untuk menyimpan User.");
    }
    @Override
    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request) {
        Map<String,Object> mapToken = JwtUtility.extractToken(request);

        if (user == null){
            return GlobalResponse.dataTidakValid("",request);
        }
        try{
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            User userNext = userOptional.get();
            modelMapper.map(user, userNext);
            userNext.setModifiedBy(Long.valueOf(mapToken.get("userId").toString()));

            // Simpan perubahan
            userRepository.save(userNext);

            return GlobalResponse.dataBerhasilDiubah(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try{
            if (!userRepository.existsById(id)){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            userRepository.deleteById(id);
            return GlobalResponse.dataBerhasilDihapus(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
    }

    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        try {
            Page<User> page = userRepository.findAll(pageable);
            List<User> list = page.getContent();

            List<RespUserDTO> responseList = convertToRespUsersDTO(list);

            return GlobalResponse.dataDitemukan(
                    transformPagination.transformPagination(responseList, page, null, null),
                    request
            );

        } catch (Exception e) {
            // Log error ke konsol atau sistem logging
            e.printStackTrace();

            // Kamu bisa membuat GlobalResponse.error / gagalDitemukan() sesuai dengan konvensimu
            return GlobalResponse.terjadiKesalahan(
                    "",
                    request
            );
        }
    }


    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<User> userOptional = Optional.empty();
        try{
            userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            return GlobalResponse.dataDitemukan(userOptional.get(),request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;

        switch (columnName){
            case "nama"->page = userRepository.findByNamaContainsIgnoreCase(value,pageable);
            case "alamat"->page = userRepository.findByAlamatContainsIgnoreCase(value,pageable);
            default ->page = userRepository.findAll(pageable);

        }
        list = page.getContent();

        List<RespUserDTO> responseList = convertToRespUsersDTO(list);

        return GlobalResponse.dataDitemukan(transformPagination.transformPagination(responseList,page,null,null),request);
    }

    public List<RespUserDTO> convertToRespUsersDTO(List<User> users){
        List<RespUserDTO> responseListDTO = new ArrayList<>();
        for (User user : users){

            RespUserDTO respUserDTO = new RespUserDTO();
            respUserDTO.setId(user.getId());
            respUserDTO.setUsername(user.getUsername());
            respUserDTO.setNama(user.getNama());
            respUserDTO.setEmail(user.getEmail());
            respUserDTO.setAlamat(user.getAlamat());
            respUserDTO.setNoHp(user.getNoHp());
            respUserDTO.setLinkImage(user.getLinkImage());
            respUserDTO.setPathImage(user.getPathImage());
            respUserDTO.setTanggalLahir(user.getTanggalLahir());
            respUserDTO.setUmur(
                    Period.between(user.getTanggalLahir(), LocalDate.now()).getYears()
            );

            responseListDTO.add(respUserDTO);
        }
        return responseListDTO;
    }

    public User convertDtoToEntity(ValUserDTO regisDTO){
        return modelMapper.map(regisDTO, User.class);
    }
}
