package com.example.common.face.dto;

import com.example.common.face.constant.FaceConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 12:12
 * Desc:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceUserDTO<T> implements Serializable {

    private String userId;

    private String groupId = FaceConstant.DEFAULT_GROUP_ID;

    private String faceToken;

    private T user;
}
