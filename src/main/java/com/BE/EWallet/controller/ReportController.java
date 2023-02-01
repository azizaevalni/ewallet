package com.BE.EWallet.controller;

import com.BE.EWallet.dto.ReportDTO;
import com.BE.EWallet.dto.ReportDTO2;
import com.BE.EWallet.model.User;
import com.BE.EWallet.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/report/getreport/{date}")
    public ResponseEntity<Object> getReport(@PathVariable("date") LocalDate date) {
        return  new ResponseEntity<>(new ReportDTO2(reportService.getreport(date)),HttpStatus.OK);
    }


}
