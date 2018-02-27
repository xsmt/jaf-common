package cn.jcloud.jaf.common.query;

import cn.jcloud.jaf.common.util.JafJsonMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JavaType;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * wrap the list query result by jpa. Use <T> to generic return object.
 *
 * @param <T>
 * @author hasayaki
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Items<T> implements Iterable<T> {

    private Long version = null;

    private Long count = null;

    private Boolean end = null;

    @JsonUnwrapped
    private Object append;

    private List<T> items = Collections.emptyList();

    private Items() {
    }

    /**
     * 使用传入的List进行构建Items
     */
    public static <T> Items<T> of(List<T> items) {
        Items<T> result = new Items<T>();
        result.items = items;
        return result;
    }

    /**
     * 使用传入的List进行构建Items，并重新指定count
     */
    public static <T> Items<T> of(List<T> items, Long count) {
        Items<T> result = of(items);
        result.count = count;
        return result;
    }

    /**
     * 使用传入的List进行构建Items，并指定是否返回count，count使用传入的list的size进行取值
     */
    public static <T> Items<T> of(List<T> items, boolean isCount) {
        Items<T> result = of(items);
        if (isCount) {
            result.count = (long) items.size();
        }
        return result;
    }

    /**
     * 使用传入的List进行构建Items，使用传入的page进行分页，count使用传入的list的size进行取值
     */
    public static <T> Items<T> of(List<T> items, SlicePage<T> page) {
        long count = 0;
        if (items != null) {
            count = items.size();
        }
        return of(items, count, page);
    }

    /**
     * 使用传入的List进行构建Items，使用传入的page进行分页，count使用传入的count进行取值
     */
    public static <T> Items<T> of(List<T> items, long count, SlicePage<T> page) {
        if (items == null || items.isEmpty()) {
            return Items.of(Collections.<T>emptyList(), page.isCount());
        }
        int start = Math.min(page.getOffset(), (int) count);
        int end = Math.min(page.getLimit() + start, (int) count);
        List<T> subList = items.subList(start, end);
        if (page.isCount()) {
            return Items.of(subList, count);
        }
        return Items.of(subList);
    }

    /**
     * 构建空的Items，并指定是否返回count
     */
    public static <T> Items<T> empty(boolean isCount) {
        return of(Collections.<T>emptyList(), isCount);
    }

    /**
     * 构建空的Items
     */
    public static <T> Items<T> empty() {
        return of(Collections.<T>emptyList());
    }

    /**
     * 当需要使用jackson解析字符串为Items对象时，使用此方法来构造带泛型的Items类型
     *
     * @param itemClass
     * @param <T>
     * @return
     */
    public static <T> JavaType constructJsonType(Class<T> itemClass) {
        return JafJsonMapper
                .getMapper()
                .getTypeFactory()
                .constructParametrizedType(Items.class, Items.class, itemClass);
    }

    public Long getVersion() {
        return version;
    }

    public Long getCount() {
        return count;
    }

    public Boolean getEnd() {
        return end;
    }

    public Object getAppend() {
        return append;
    }

    public void setAppend(Object append) {
        this.append = append;
    }

    public List<T> getItems() {
        return items;
    }

    public int size() {
        return this.items.size();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return this.items.iterator();
    }
}
