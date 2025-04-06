package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.response.RespGroupMenuDTO;
import com.fdn.course.monitoring.dto.validation.ValGroupMenuDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.GroupMenu;
import com.fdn.course.monitoring.repository.GroupMenuRepository;
import com.fdn.course.monitoring.repository.MenuRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class GroupMenuService implements IService<GroupMenu> {

    @Autowired
    private GroupMenuRepository groupMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformPagination transformPagination;

    StringBuilder sBuild = new StringBuilder();



    @Override
    public ResponseEntity<Object> save(GroupMenu groupMenu, HttpServletRequest request) {
        Map<String,Object> mapToken = JwtUtility.extractToken(request);
        try {
            groupMenu.setCreatedBy(Long.parseLong(mapToken.get("userId").toString()));
            groupMenuRepository.save(groupMenu);
        }catch (Exception e) {
//            LoggingFile.logException("GroupMenuService","save(GroupMenu groupMenu, HttpServletRequest request) -- Line 59 "+RequestCapture.allRequest(request),e,OtherConfig.getEnableLog());
            return GlobalResponse.dataGagalDisimpan("USM01FE001",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, GroupMenu groupMenu, HttpServletRequest request) {
        Map<String,Object> mapToken = JwtUtility.extractToken(request);
        if (groupMenu == null) {
            return GlobalResponse.dataTidakValid("USM01FV011",request);
        }

        try {
            Optional<GroupMenu> optionalGroupMenu = groupMenuRepository.findById(id);
            if (optionalGroupMenu.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("USM01FV012",request);
            }

            GroupMenu nextGroupMenu = optionalGroupMenu.get();
            nextGroupMenu.setModifiedBy(Long.parseLong(mapToken.get("userId").toString()));
            nextGroupMenu.setNama(groupMenu.getNama());
            nextGroupMenu.setDeskripsi(groupMenu.getDeskripsi());

        }catch (Exception e) {
//            LoggingFile.logException("GroupMenuService","update(Long id, GroupMenu groupMenu, HttpServletRequest request) -- Line 81 "+RequestCapture.allRequest(request),e,OtherConfig.getEnableLog());
            return GlobalResponse.dataGagalDiubah("USM01FE011",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<GroupMenu> groupMenuOptional = groupMenuRepository.findById(id);
            if (groupMenuOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("USM01FV021",request);
            }

            GroupMenu groupMenu = groupMenuOptional.get();
            menuRepository.deleteByGroupMenu(groupMenu);
            groupMenuRepository.deleteById(id);
        }catch (Exception e) {
//            LoggingFile.logException("GroupMenuService","delete(Long id) -- Line 95 "+RequestCapture.allRequest(request),e,OtherConfig.getEnableLog());
            return GlobalResponse.dataGagalDihapus("USM01FE021",request);
        }
        return GlobalResponse.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<GroupMenu> page = null;
        List<GroupMenu> list = null;
        page = groupMenuRepository.findAll(pageable);
        list = page.getContent();
        List<RespGroupMenuDTO> lt = convertToRespGroupMenuDTO(list);

        return GlobalResponse.dataDitemukan(transformPagination.transformPagination(lt,page,null,null),
                request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<GroupMenu> optionalGroupMenu = Optional.empty();
        try {
            optionalGroupMenu = groupMenuRepository.findById(id);
            if (optionalGroupMenu.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("USM01FV041",request);
            }
        }catch (Exception e) {
//            LoggingFile.logException("GroupMenuService","findById(Long id, HttpServletRequest request) -- Line 122 "+RequestCapture.allRequest(request),e,OtherConfig.getEnableLog());
            return GlobalResponse.terjadiKesalahan("USM01FE041",request);
        }
        return GlobalResponse.dataDitemukan(modelMapper.map(optionalGroupMenu.get(),RespGroupMenuDTO.class),request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {

        Page<GroupMenu> page = null;
        List<GroupMenu> list = null;
        switch (columnName) {
            case "nama": page = groupMenuRepository.findByNamaContainsIgnoreCase(value,pageable);break;
            case "deskripsi": page = groupMenuRepository.findByDeskripsiContainsIgnoreCase(value,pageable);break;
            default: page = groupMenuRepository.findAll(pageable);
        }
        list = page.getContent();
        List<RespGroupMenuDTO> lt = convertToRespGroupMenuDTO(list);

        return GlobalResponse.dataDitemukan(transformPagination.transformPagination(lt,page,columnName,value),
                request);
    }



    public List<RespGroupMenuDTO> convertToRespGroupMenuDTO(List<GroupMenu> groupMenus) {

        return modelMapper.map(groupMenus, new TypeToken<List<RespGroupMenuDTO>>() {}.getType());
    }

    public GroupMenu convertDtoToEntity(ValGroupMenuDTO valGroupMenuDTO) {

        return modelMapper.map(valGroupMenuDTO, GroupMenu.class);
    }
}