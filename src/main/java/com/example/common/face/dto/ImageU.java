package com.example.common.face.dto;

import com.example.common.face.constant.ImageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:25
 * Desc: 图像对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageU implements Serializable {

    private ImageTypeEnum imageTypeEnum;

    private String data;
}
