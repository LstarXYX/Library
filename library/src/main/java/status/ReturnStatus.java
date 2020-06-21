package status;

import lombok.Getter;

/**
 * @author L.star
 * @date 2020/6/1 20:00
 */
@Getter
public enum ReturnStatus {
    /**
     * 状态码
     * 200 正常
     * 201 黑名单用户
     * 202 超过可借书数目
     * 205 文件类型不支持
     *
     * 500 错误
     */
    NORMAL(200),ERROR(500),ERRORTYPE(205),BLACKUSER(201),OUTOFNUM(202);

    private int statusCode;
    private String resultStatus;
    ReturnStatus(int statusCode) {
        this.statusCode = statusCode;
        this.resultStatus =  "{\"status\": " + this.statusCode + "}";
    }

    ReturnStatus() {
    }
}
