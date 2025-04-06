package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.validation.ValMenuDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.GroupMenu;
import com.fdn.course.monitoring.model.Menu;
import com.fdn.course.monitoring.repository.GroupMenuRepository;
import com.fdn.course.monitoring.repository.MenuRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MenuService implements IService<Menu> {

    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private GroupMenuRepository groupMenuRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(Menu menu, HttpServletRequest request) {
        Map<String,Object> mapToken = JwtUtility.extractToken(request);
        try {
            menu.setCreatedBy(Long.parseLong(mapToken.get("userId").toString()));
            menuRepository.save(menu);
        }catch (Exception e) {
//            LoggingFile.logException("GroupMenuService","save(GroupMenu groupMenu, HttpServletRequest request) -- Line 59 "+RequestCapture.allRequest(request),e,OtherConfig.getEnableLog());
            return GlobalResponse.dataGagalDisimpan("",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Menu menu, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try{
            if (!menuRepository.existsById(id)){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            menuRepository.deleteById(id);
            return GlobalResponse.dataBerhasilDihapus(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        return null;
    }

    public Menu convertDtoToEntity(ValMenuDTO menuDTO){
        Menu menu = modelMapper.map(menuDTO, Menu.class);
        GroupMenu groupMenu = groupMenuRepository.getReferenceById(menuDTO.getGroupMenu().getId());
        menu.setGroupMenu(groupMenu);
        return menu;
    }
}
