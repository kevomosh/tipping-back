package com.kakuom.finaltipping.responses;

import com.kakuom.finaltipping.dto.GroupDTO;

import java.util.List;

public class JwtResponse {
    private String token;
    private String username;
    private Long id;
    private List<GroupDTO> nrlGroups;
    private List<GroupDTO> aflGroups;

    public JwtResponse(String token, String username, Long id, List<GroupDTO> nrlGroups, List<GroupDTO> aflGroups) {
        this.token = token;
        this.username = username;
        this.id = id;
        this.nrlGroups = nrlGroups;
        this.aflGroups = aflGroups;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<GroupDTO> getNrlGroups() {
        return nrlGroups;
    }

    public void setNrlGroups(List<GroupDTO> nrlGroups) {
        this.nrlGroups = nrlGroups;
    }

    public List<GroupDTO> getAflGroups() {
        return aflGroups;
    }

    public void setAflGroups(List<GroupDTO> aflGroups) {
        this.aflGroups = aflGroups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
