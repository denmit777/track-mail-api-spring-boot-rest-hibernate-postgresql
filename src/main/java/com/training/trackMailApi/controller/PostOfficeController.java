package com.training.trackMailApi.controller;

import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.PostOffice;
import com.training.trackMailApi.service.PostOfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/post-offices")
@Api("Post office controller")
public class PostOfficeController {

    private final PostOfficeService postOfficeService;

    @PostMapping
    @ApiOperation(value = "Create a new post office")
    public ResponseEntity<?> save(@RequestBody @Valid PostOfficeDto postOfficeDto) {
        PostOffice savedPostOffice = postOfficeService.save(postOfficeDto);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedPostOfficeLocation = currentUri + "/" + savedPostOffice.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedPostOfficeLocation)
                .body(savedPostOffice);
    }

    @GetMapping
    @ApiOperation(value = "Get all postOffices")
    public ResponseEntity<List<PostOfficeDto>> getAll() {
        return ResponseEntity.ok(postOfficeService.getAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get postOffice by ID")
    public ResponseEntity<PostOfficeDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postOfficeService.getById(id));
    }
}
