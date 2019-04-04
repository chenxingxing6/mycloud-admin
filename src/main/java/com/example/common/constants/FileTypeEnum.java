package com.example.common.constants;

/**
 * User: lanxinghua
 * Date: 2019/3/17 15:02
 * Desc: 文件类型枚举
 */
public enum FileTypeEnum {
    ISO(0, "iso"),
    EXE(1, "exe"),
    HTML(2, "html"),
    JS(3, "js"),
    CSS(4, "css"),
    VUE(5, "vue"),
    JAVA(6, "java"),
    CLASS(7, "class"),
    JAR(8, "jar"),
    PDF(9, "pdf"),
    DOC(10, "doc"),
    DOCX(11, "docx"),
    PPT(12, "ppt"),
    PPTX(13, "pptx"),
    MP3(14, "mp3"),
    WAV(15, "wav"),
    MP4(16, "mp4"),
    PNG(17, "png"),
    JPG(18, "jpg"),
    JPEG(19, "jpeg"),
    BMP(20, "bmp"),
    PSD(21, "psd"),
    SWF(22, "swf"),
    GIF(23, "gif"),
    AVI(24, "avi"),
    TXT(25, "txt"),
    XLS(26, "xls"),
    XLSX(27, "xlsx"),
    ZIP(28, "zip"),
    RAR(29, "rar"),
    JSON(30, "json"),
    ;

    FileTypeEnum(Integer type, String value){
        this.type = type;
        this.value = value;
    }

    private Integer type;

    private String value;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
