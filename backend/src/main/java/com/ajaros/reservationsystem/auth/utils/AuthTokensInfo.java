package com.ajaros.reservationsystem.auth.utils;

import com.ajaros.reservationsystem.users.dtos.UserInformationResponse;

public record AuthTokensInfo(
    String accessToken, String refreshToken, UserInformationResponse user) {}
