package dora.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dora.widget.tableview.GridLayoutManager;
import dora.widget.tableview.R;
import dora.widget.tableview.ScrollTableViewAdapter;
import dora.widget.tableview.ScrollTableViewDivider;

public class DoraScrollTableView extends RecyclerView {

    public boolean headersOnTop;

    public boolean scrollingEnabled;

    public int headerBordersColor;
    public int headerBackgroundColor;

    public int dataBordersColor;
    public int dataBackgroundColor;

    public int radius;

    public int padding;

    public String[] headers;

    public List<String[]> data;

    public int columnCount;

    public DoraScrollTableView(Context context) {
        super(context);
    }

    public DoraScrollTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DoraScrollTableView);
        headerBordersColor = ta.getColor(
                R.styleable.DoraScrollTableView_dview_stv_headerBorderColor,
                Color.parseColor("#9E9E9E")
        );
        headerBackgroundColor = ta.getColor(
                R.styleable.DoraScrollTableView_dview_stv_headerBackgroundColor,
                Color.parseColor("#E0E0E0")
        );
        dataBordersColor = ta.getColor(
                R.styleable.DoraScrollTableView_dview_stv_dataBorderColor,
                Color.parseColor("#9E9E9E")
        );
        dataBackgroundColor = ta.getColor(
                R.styleable.DoraScrollTableView_dview_stv_dataBackgroundColor,
                Color.WHITE
        );
        radius = ta.getInteger(R.styleable.DoraScrollTableView_dview_stv_radius, 0);
        padding = ta.getInteger(R.styleable.DoraScrollTableView_dview_stv_padding, 0);
        headersOnTop = ta.getBoolean(R.styleable.DoraScrollTableView_dview_stv_headersOnTop, true);
        scrollingEnabled = ta.getBoolean(R.styleable.DoraScrollTableView_dview_stv_scrollingEnabled, true);
        if (!scrollingEnabled) {
            setClickable(false);
            setFocusable(false);
            setNestedScrollingEnabled(false);
        }
        ta.recycle();
        data = new ArrayList<>();
    }

    public DoraScrollTableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public void setItems(List<List<Cell>> items) {
//
//        headers = new ArrayList<>();
//        data = new ArrayList<>();
//
//        tableViewAdapter.setItems(this);
//
//        if (tv_headersOnTop) {
//            columnCount = items.size();
//
//            count = 0;
//            for (List<Cell> column : items) {
//                count += column.size();
//
//                headers.add(column.get(0));
//                column.remove(0);
//
//                data.add(column);
//            }
//
//        } else {
//            columnCount = items.size();
//
//            count = items.get(0).size();
//
//            headers.addAll(items.get(0));
//            items.remove(0);
//
//            for (List<Cell> column : items) {
//                count += column.size();
//                data.add(column);
//            }
//
//        }
//
//        setLayoutManager(new CustomGridLayoutManager(getContext(), columnCount, tv_scrollingEnabled));
//
//        addItemDecoration(new TableViewDivider(this));
//    }

    /**
     * 设置表头。
     */
    public DoraScrollTableView setHeader(String... headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置表格内容。
     */
    public DoraScrollTableView setContent(List<String[]> content) {
        data = content;
        return this;
    }

    /**
     * 为表格增加行。
     */
    public DoraScrollTableView addRowContent(String... strings) {
        data.add(strings);
        return this;
    }

    /**
     * 设置 adapter 刷新显示。
     */
    public void refreshTable() {
        if (headers == null) {
            headersOnTop = false;
        }
        if (headers != null) {
            columnCount = headers.length;
        } else if (!data.isEmpty()) {
            columnCount = data.get(0).length;
        } else {
            columnCount = 0;
        }
        ScrollTableViewAdapter adapter = new ScrollTableViewAdapter(this);
        setAdapter(adapter);
        setLayoutManager(new GridLayoutManager(getContext(), columnCount, scrollingEnabled));
        addItemDecoration(new ScrollTableViewDivider(this));
    }

    public interface OnTableItemClickListener {
        void itemClickListener(String content, int row, int column);
    }

    public OnTableItemClickListener listener;

    public void setOnTableItemClickListener(OnTableItemClickListener tableItemClickListener) {
        listener = tableItemClickListener;
    }
}