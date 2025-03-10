package com.uday.service;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}

