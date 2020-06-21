package status;


/**
 * 图书借出的状态信息
 *
 * @author L.star
 * @date 2020/5/29 16:11
 */
public enum BookLendStatus {
    /**
     * 四种状态
     */
    Lending("借出"), TIMEOUT("超期"), LOSING("丢失"), BREAK("损坏"),RETURNED("归还");

    private String status;

    BookLendStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
