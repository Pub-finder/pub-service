package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.service.PubService;

import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Pubs controller.
 */
@RestController
@RequestMapping("/pub")
public class PubController {

    @Autowired
    private PubService pubService;

    @GetMapping(value = "/getPubs/{lat}/{lng}/{radius}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PubDto>> getPubs(@PathVariable("lat") Double lat,
                                                @PathVariable("lng") Double lng, @PathVariable("radius") Double radius) {
        return ResponseEntity.ok().body(pubService.getPubs(lat, lng, radius));
    }

    @GetMapping("/getPub/{id}")
    public ResponseEntity<PubDto> getPub(@PathVariable("id") UUID id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(pubService.getPub(id));
    }

    @GetMapping("/searchPubs/{term}")
    public ResponseEntity<List<PubDto>> searchPubsByTerm(@PathVariable("term") String term) {
        return ResponseEntity.ok(pubService.searchPubsByTerm(term));
    }

    @PostMapping("/createPub")
    public ResponseEntity<PubDto> save(@RequestBody PubDto pub) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pubService.save(Mapper.INSTANCE.dtoToEntity(pub)));
    }

    @PostMapping("/createPubs")
    public ResponseEntity<List<PubDto>> savePubs(@RequestBody List<PubDto> pubs) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pubService.savePubs(pubs.stream().map(Mapper.INSTANCE::dtoToEntity).toList()));
    }

    @PutMapping("/editPub")
    public ResponseEntity<PubDto> edit(@RequestBody PubDto pub)
            throws ResourceNotFoundException, BadRequestException {
        return ResponseEntity.ok().body(pubService.edit(Mapper.INSTANCE.dtoToEntity(pub)));
    }

    @DeleteMapping("/deletePub")
    public ResponseEntity<Void> delete(@RequestBody PubDto pub) throws BadRequestException {
        pubService.delete(Mapper.INSTANCE.dtoToEntity(pub));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<AdditionalInfoDto> getAdditionalInfo(@PathVariable("id") UUID id)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(pubService.getAdditionalInfo(id));
    }
}
