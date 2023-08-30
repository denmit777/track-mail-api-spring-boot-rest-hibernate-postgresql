package com.training.trackMailApi.controller;

import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.service.HistoryService;
import com.training.trackMailApi.service.MailingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/mailings")
@Api("Mailing controller")
public class MailingController {

    private final MailingService mailingService;
    private final HistoryService historyService;

    @PostMapping
    @ApiOperation(value = "Register new mailing", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<?> register(@RequestBody MailingDto mailingDto, Principal principal) {
        Mailing savedMailing = mailingService.register(mailingDto, principal.getName());

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedMailingLocation = currentUri + "/" + savedMailing.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedMailingLocation)
                .body(savedMailing);
    }

    @PutMapping("{mailingId}/arriveToPostOffice/{postOfficeId}")
    @ApiOperation(value = "Arrive mailing to Post office")
    public ResponseEntity<String> arriveToPostOffice(@PathVariable("mailingId") Long mailingId,
                                                     @PathVariable("postOfficeId") Long postOfficeId) {
        mailingService.arriveToPostOffice(mailingId, postOfficeId);

        return ResponseEntity.ok("Mailing arrived to Post office");
    }

    @PutMapping("{mailingId}/leavePostOffice/{postOfficeId}")
    @ApiOperation(value = "Departure mailing from Post office")
    public ResponseEntity<String> leavePostOffice(@PathVariable("mailingId") Long mailingId,
                                                  @PathVariable("postOfficeId") Long postOfficeId) {
        mailingService.leavePostOffice(mailingId, postOfficeId);

        return ResponseEntity.ok("Mailing left Post office");
    }

    @PutMapping("{mailingId}/receiveByRecipient")
    @ApiOperation(value = "Receive mailing by recipient", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<String> receiveByRecipient(@PathVariable("mailingId") Long mailingId, Principal principal) {
        mailingService.receiveByRecipient(mailingId, principal.getName());

        return ResponseEntity.ok("Mailing received by recipient");
    }

    @GetMapping
    @ApiOperation(value = "Get all mailings")
    public ResponseEntity<List<MailingDto>> getAll() {
        return ResponseEntity.ok(mailingService.getAll());
    }

    @GetMapping("/{mailingId}")
    @ApiOperation(value = "Get mailing by ID")
    public ResponseEntity<MailingDto> getById(@PathVariable("mailingId") Long mailingId) {
        return ResponseEntity.ok(mailingService.getById(mailingId));
    }

    @GetMapping("{mailingId}/history")
    @ApiOperation(value = "Get all history about mailing")
    public ResponseEntity<List<HistoryDto>> getAllHistoryByMailingId(@PathVariable("mailingId") Long mailingId) {
        List<HistoryDto> history = historyService.getAllByMailingId(mailingId);

        return ResponseEntity.ok(history);
    }
}
