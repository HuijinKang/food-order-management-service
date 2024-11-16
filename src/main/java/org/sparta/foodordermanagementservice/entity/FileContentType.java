package org.sparta.foodordermanagementservice.entity;

public enum FileContentType {
    JPG("image/jpg"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    GIF("image/gif");

    private final String type;

    FileContentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static FileContentType getContentType(String type){
        for(FileContentType index : FileContentType.values()){
            if(index.getType().equals(type)){
                return index;
            }
        }
        return null;  // 유효한 이미지 파일 타입이 아닐 경우 null 반환
    }
}
