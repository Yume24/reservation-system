package com.ajaros.reservationsystem.auth.utils;

import com.ajaros.reservationsystem.users.dtos.UserInformation;

public record AuthTokensInfo(String accessToken, String refreshToken, UserInformation user) {}
