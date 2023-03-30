package minesweeper;

/**
 * 扫雷所用的棋盘上的格子。
 *
 * @author sichengchen
 */

public class Cell {
    private boolean covered; //是否未被打开
    private boolean marked; //是否被标记（右键）
    private boolean mine; //是否为雷
    private int value; //棋盘上显示的数组（周边雷数）
    private boolean checked;

    public Cell() {
        this.covered = true;
        this.marked = false;
        this.mine = false;
        this.value = 0;
        this.checked = false;
    }

    public int getValue() {
        return value;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isCovered() {
        return covered;
    }

    public boolean isMarked() {
        return marked;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isEmpty() {
        return this.value == 0;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void uncover() {
        setCovered(false);
    }
}
