/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-02-09 23:41:18
 *
 */

package com.gcssloop.diycode_sdk.api.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token implements Serializable {
  @SerializedName("access_token") private String accessToken;
  @SerializedName("token_type") private String tokenType;
  @SerializedName("expires_in") private int expiresIn;
  @SerializedName("refresh_token") private String refreshToken;
  @SerializedName("created_at") private int createdAt;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public int getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(int createdAt) {
    this.createdAt = createdAt;
  }

  @Override public String toString() {
    return "Token{" +
        "accessToken='" + accessToken + '\'' +
        ", tokenType='" + tokenType + '\'' +
        ", expiresIn=" + expiresIn +
        ", refreshToken='" + refreshToken + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
