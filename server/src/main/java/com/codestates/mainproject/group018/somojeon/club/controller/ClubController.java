package com.codestates.mainproject.group018.somojeon.club.controller;

import com.codestates.mainproject.group018.somojeon.club.dto.ClubDto;
import com.codestates.mainproject.group018.somojeon.club.entity.Club;
import com.codestates.mainproject.group018.somojeon.club.mapper.ClubMapper;
import com.codestates.mainproject.group018.somojeon.club.service.ClubService;
import com.codestates.mainproject.group018.somojeon.dto.ClubCategoryResponseDtos;
import com.codestates.mainproject.group018.somojeon.dto.MultiResponseDto;
import com.codestates.mainproject.group018.somojeon.dto.SingleResponseDto;
import com.codestates.mainproject.group018.somojeon.record.entity.Record;
import com.codestates.mainproject.group018.somojeon.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final ClubMapper mapper;

    public ClubController(ClubService clubService, ClubMapper mapper) {
        this.clubService = clubService;
        this.mapper = mapper;
    }

    // 소모임 생성
    @PostMapping
    public ResponseEntity postClub(@Valid @RequestBody ClubDto.Post requestBody) {

        Club createdClub = clubService.createClub(mapper.clubPostDtoToClub(requestBody), requestBody.getTagName());
        URI location = UriCreator.createUri("/clubs", createdClub.getClubId());

        return ResponseEntity.created(location).build();
    }

    // 소모임 수정 (소개글, 이미지 등)
    @PatchMapping("/{club-id}")
    public ResponseEntity patchClub(@PathVariable("club-id") @Positive Long clubId,
                                    @RequestBody @Valid ClubDto.Patch requestBody) {

        requestBody.setClubId(clubId);
        Club response = clubService.updateClub(
                mapper.clubPatchDtoToClub(requestBody), requestBody.getTagName());

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.clubToClubResponse(response)), HttpStatus.OK);
    }

    // 퍼블릭 소모임 단건 조회
    @GetMapping("/{club-id}")
    public ResponseEntity getClub(@PathVariable("club-id") @Positive Long clubId) {

        Club findClub = clubService.findClub(clubId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.clubToClubResponse(findClub)), HttpStatus.OK);
    }

    // 퍼블릭 소모임 전체 조회
    @GetMapping
    public ResponseEntity getClubs(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {

        Page<Club> clubPage = clubService.findClubs(page - 1, size);
        List<Club> content = clubPage.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.clubToClubResponseDtos(content), clubPage), HttpStatus.OK);
    }

    // 키워드로 퍼블릭 소모임 찾기
    @GetMapping("/search")
    public ResponseEntity searchClubs(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam String keyword) {

        Page<Club> clubPage = clubService.searchClubs(page - 1, size, keyword);
        List<Club> content = clubPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.clubToClubResponseDtos(content), clubPage), HttpStatus.OK);
    }

    // 카테고리별로 소모임 조회
    @GetMapping("/categories")
    public ResponseEntity findClubByCategoryName(@RequestParam String categoryName) {
        List<Club> allClubByCategoryName = clubService.findAllClubByCategoryName(categoryName);

        return new ResponseEntity<>(
                new ClubCategoryResponseDtos(
                        mapper.clubToClubResponseDtos(allClubByCategoryName)), HttpStatus.OK);
    }

    // 소모임 삭제
    @DeleteMapping("/{club-id}")
    public ResponseEntity deleteClub(@PathVariable("club-id") @Positive Long clubId) {
        clubService.deleteClub(clubId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 내 소모임 찾기
    @GetMapping("/my")
    public ResponseEntity getMyClub() {
        return null;
    }

    // TODO: admin 컨트롤러로 가야함. 소모임 상태 변경
    @PatchMapping("/{club-id}/club-status")
    public ResponseEntity patchClubStatus() {
        return null;
    }

    // 소모임 권한 수정
    @PatchMapping("/manage/{user-id}")
    public ResponseEntity patchMange() {
        return null;
    }

    // 소모임 리더 위임
    @PatchMapping("/manage/{user-id}/leader")
    public ResponseEntity patchClubLeader() {
        return null;
    }

    // 소모임 멤버 상태 변경
    @PatchMapping("/{user-id}/member-status")
    public ResponseEntity patchClubMemberStatus() {
        return null;
    }

    // 소모임 멤버 회원 탈퇴
    @PatchMapping("/{user-id}/member-quit")
    public ResponseEntity patchMemberQuitClub() {
        return null;
    }


}
