package lab.ia.ExpenseManagement.Payloads.Response;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PagedResponse<T> {
    private List<T> content;

    private int page;

    private int size;

    private int totalPages;

    private long totalElements;

    private boolean last;

    public PagedResponse(List<T> content, int page, int size, int totalPages, long totalElements, boolean last) {
//        this.setContent(content);
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
    }

    public void setContent(List<T> content) {
        if (content == null)
            this.content = null;
        else
            this.content = Collections.unmodifiableList(content);
    }
}
