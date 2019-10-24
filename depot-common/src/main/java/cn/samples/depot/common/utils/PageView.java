package cn.samples.depot.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

@SuppressWarnings("unused")
public class PageView<T> implements IPage<T> {
    private static final long serialVersionUID = 7795075947955705706L;
    public static final int SHOW_PAGES = 11;

    public interface View {
    }

    private IPage<T> _page;
    private int indicator = SHOW_PAGES;

    private PageView() {
    }

    @JsonView(View.class)
    public static <T> PageView<T> wrap(IPage<T> page) {
        PageView<T> vp = new PageView<>();
        vp._page = page;
        return vp;
    }

    @JsonView(View.class)
    public static <T> PageView<T> wrap(IPage<T> page, int indicator) {
        PageView<T> vp = wrap(page);
        vp.indicator = indicator;
        return vp;
    }

    @JsonView(View.class)
    @Override
    public List<T> getRecords() {
        return _page.getRecords();
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        return _page.setRecords(records);
    }

    @JsonView(View.class)
    @Override
    public long getTotal() {
        return _page.getTotal();
    }

    @Override
    public IPage<T> setTotal(long total) {
        return _page.setTotal(total);
    }

    @JsonView(View.class)
    @Override
    public long getSize() {
        return _page.getSize();
    }

    @Override
    public IPage<T> setSize(long size) {
        return _page.setSize(size);
    }

    @JsonView(View.class)
    @Override
    public long getCurrent() {
        return _page.getCurrent();
    }

    @Override
    public IPage<T> setCurrent(long current) {
        return _page.setCurrent(current);
    }
}
