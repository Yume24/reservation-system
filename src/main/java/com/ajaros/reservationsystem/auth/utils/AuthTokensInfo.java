package com.ajaros.reservationsystem.auth.utils;

import com.ajaros.reservationsystem.users.entities.User;

public record AuthTokensInfo(String accessToken, String refreshToken, User user) {}
