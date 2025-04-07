package com.fdn.course.monitoring.util;

import com.fdn.course.monitoring.dto.response.MenuLoginDTO;
import com.fdn.course.monitoring.dto.response.MenuMappingDTO;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransformationDataManual {
    private int intListMenuSize = 0;
    private final List<Object> listObject = new ArrayList<>();
    private ModelMapper modelMapper = new ModelMapper();

    public List<Object> doTransformAksesMenuLogin(List<MenuLoginDTO> ltMenu) {
        listObject.clear();
        intListMenuSize = ltMenu.size();

        List<MenuMappingDTO> menuMapping = convertToMenuMappingDTO(ltMenu);
        Map<String, List<MenuMappingDTO>> tempMap = groupBy(menuMapping, MenuMappingDTO::getNamaGroupMenu);

        Map<String, Object> mapResponse = null;
        for (Map.Entry<String,List<MenuMappingDTO>> map : tempMap.entrySet()){
            mapResponse = new LinkedHashMap<>();
            mapResponse.put("group", map.getKey());
            mapResponse.put("subMenu", map.getValue());
            listObject.add(mapResponse);
        }
        return listObject;
    }

    public <E, K> Map<K, List<E>> groupBy(List<E> list, Function<E, K> keyFunction){
        return Optional.ofNullable(list)
                .orElseGet(ArrayList::new)
                .stream().skip(0)
                .collect(Collectors.groupingBy(keyFunction,Collectors.toList()));
    }

    private List<MenuMappingDTO> convertToMenuMappingDTO(List<MenuLoginDTO> ltMenuLogin) {
        List<MenuMappingDTO> ltMenuMapping = new ArrayList<>();
        for (MenuLoginDTO menuLoginDTO : ltMenuLogin) {
            MenuMappingDTO mapping = new MenuMappingDTO();
            mapping.setNama(menuLoginDTO.getNama());
            mapping.setPath(menuLoginDTO.getPath());
            mapping.setNamaGroupMenu(menuLoginDTO.getGroupMenu().getNamaGroupMenu());
            ltMenuMapping.add(mapping);
        }
        return ltMenuMapping;
    }
}