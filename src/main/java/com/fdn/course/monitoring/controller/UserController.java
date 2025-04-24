package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValUserDTO;
import com.fdn.course.monitoring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    //untuk mengambil semua list peserta
    @GetMapping("/user-list")
    @PreAuthorize("hasAuthority('Daftar Pengguna')")
    public ResponseEntity<Object>findAllAsAdmin(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,20, Sort.by("id"));
        return userService.findAll(pageable,request);
    }

    //Mencari data user
    @GetMapping("/{sort}/{sortBy}/{page}")
    @PreAuthorize("hasAuthority('Daftar Pengguna')")
    public ResponseEntity<Object> findAllByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            HttpServletRequest request
    ) {
        Pageable pageable = null;
        sortBy = sortColumnByMap(sortBy);
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        return userService.findByParam(pageable,column,value,request);
    }

    @GetMapping("/list-peserta/{sort}/{sortBy}/{page}")
    @PreAuthorize("hasAuthority('Daftar Pengguna')")
    public ResponseEntity<Object> findPesertaByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            HttpServletRequest request
    ) {
        Pageable pageable = null;
        sortBy = sortColumnByMap(sortBy);
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        return userService.findByParam(pageable,column,value,request);
    }

    //Menghapus data user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id, HttpServletRequest request){
        return userService.delete(id,request);
    }

    //Update profile user
    //akses nya untuk admin dan peserta
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('edit-profile)")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody ValUserDTO user,
            HttpServletRequest request){
        return userService.update(id, userService.convertDtoToEntity(user), request);
    }



    private String sortColumnByMap(String sortBy){
        switch (sortBy){
            case "nama":sortBy = "nama";break;
            case "deskripsi":sortBy = "deskripsi";break;
            default:sortBy = "id";break;
        }
        return sortBy;
    }}
