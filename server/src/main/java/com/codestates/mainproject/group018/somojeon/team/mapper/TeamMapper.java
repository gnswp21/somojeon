package com.codestates.mainproject.group018.somojeon.team.mapper;

import com.codestates.mainproject.group018.somojeon.team.dto.TeamDto;
import com.codestates.mainproject.group018.somojeon.team.entity.Team;
import com.codestates.mainproject.group018.somojeon.team.entity.UserTeam;
import com.codestates.mainproject.group018.somojeon.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {
    Team teamPostDtoToTeam(TeamDto.Post requestBody);
    Team teamPatchDtoToTeam(TeamDto.Patch requestBody);
//    TeamDto.Response teamToTeamResponseDto(Team team);
    default TeamDto.Response teamToTeamResponseDto(Team team) {
        if (team == null) {
            return null;
        }

        return TeamDto.Response
                .builder()
                .teamId(team.getTeamId())
                .score(team.getScore())
                .winLoseDraw(team.getWinLoseDraw())
                .users(userTeamsToUserTeamResponseDtos(team.getUserTeams()))
                .build();
    }

    default List<UserDto.Response> userTeamsToUserTeamResponseDtos(List<UserTeam> userTeams) {
        return userTeams.stream()
                .map(userTeam -> {
                    UserDto.Response response = new UserDto.Response();
                    response.setUserId(userTeam.getUser().getUserId());
                    response.setNickName(userTeam.getUser().getNickName());
//                    response.setEmail(userTeam.getUser().getEmail());
//                    response.setUserStatus(userTeam.getUser().getUserStatus());

                    return response;
                })
                .collect(Collectors.toList());
    }

    List<TeamDto.Response> teamsToTeamResponseDtos(List<Team> teamList);
}
