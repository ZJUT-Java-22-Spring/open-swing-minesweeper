package minesweeper;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.awt.event.*;

import java.util.Random;

/**
 * 扫雷所用的棋盘。
 *
 * @author sichengchen
 */
public class Board extends JPanel {
    // Constants
    public static final int CELL_SIZE = 15;
    private final int IMAGES_NUM = 13;
    private final int FACE_IMAGES_NUM = 4;

    private final int IMAGE_MINE = 9;
    private final int IMAGE_COVERED = 10;
    private final int IMAGE_MARKED = 11;
    private final int IMAGE_MARK_WRONG = 12;

    private final int FACE_GRINNING = 0;
    private final int FACE_HUSHED = 1;
    private final int FACE_SUNGLASSES = 2;
    private final int FACE_CRYING = 3;

    // GUI elements
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JLabel pictureLabel;

    // Variables
    private int totalMines = 40; // 后期改用，暂不 final
    private int minesRemaining;

    private int rows = 16; // 后期改用，暂不 final
    private int columns = 16; // 后期改用，暂不 final

    private Cell[][] cells;
    private ImageIcon[] imageIcons;
    private ImageIcon[] faceIcons;
    private JLabel[][] imageLabels;

    Random random = new Random();

    private boolean inGame;
    private boolean isFirstPress;

    /**
     * 构造方法。
     *
     */
    public Board(JLabel statusLabel, JLabel timeLabel, JLabel pictureLabel) {
        this.imageIcons = new ImageIcon[IMAGES_NUM];
        this.faceIcons = new ImageIcon[FACE_IMAGES_NUM];
        this.pictureLabel = pictureLabel;

        // Images initialization
        for(int i = 0; i < IMAGES_NUM; i++) {
            java.net.URL imgURL = getClass().getResource("/res/img/" + i + ".gif");
            assert imgURL != null;
            imageIcons[i] = new ImageIcon(imgURL);
        }

        for(int i = 0; i < FACE_IMAGES_NUM; i++) {
            java.net.URL imgURL = getClass().getResource("/res/img/F" + i + ".gif");
            assert imgURL != null;
            faceIcons[i] = new ImageIcon(imgURL);
        }

        this.statusLabel = statusLabel;
        this.timeLabel = timeLabel;

        Minesweeper.setUpdateTaskTimeLabel(timeLabel);
        Minesweeper.updateTask.disable();
        UpdateTask.clear();

        addMouseListener(new MouseEventHandler());
    }

    /**
     * 计算格子内需要标识的数字（周边雷数）。
     *
     * @param x 需要计算的格子横坐标
     * @param y 需要计算的格子纵坐标
     * @return 计算所得的数
     */
    public int countCellValues(int x, int y) {
        int count = 0;

        for(int i = -1; i <= 1; i++) {
            int fixedX = x + i; // 计算绝对坐标
            if(fixedX < 0 || fixedX >= this.rows) {
                continue; // 此时越界
            }
            for(int j = -1; j <= 1; j++) {
                int fixedY = y + j; // 计算绝对坐标
                if(fixedY < 0 || fixedY >= this.columns) {
                    continue; // 此时越界
                }
                if(i == j && i == 0) {
                    continue; // 自身无需计算
                }
                if(this.cells[fixedX][fixedY].isMine()) {
                    count++; // 进行计数
                }
            }
        }

        return count;
    }

    /**
     * 将计算出来的雷数写入对应格子的 value 变量中。
     */
    public void setCellValues() {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.columns; j++) {
                if(!this.cells[i][j].isMine()) {
                    this.cells[i][j].setValue(countCellValues(i, j));
                }
            }
        }
    }

    /**
     * 遍历棋盘，找出从点击位置开始的空白区域，并打开各自的格子。
     *
     * @param x 开始，和后续传入的位置坐标
     * @param y 开始，和后续传入的位置坐标
     * @param d 深度，用于判断遍历是否结束（深度为0）
     */
    public void findEmptyCells(int x, int y, int d) {
        for(int i = -1; i <= 1; i++) {
            int fixedX = x + i; // 计算绝对坐标
            if(fixedX < 0 || fixedX >= this.rows) {
                continue; // 此时越界
            }
            for(int j = -1; j <= 1; j++) {
                int fixedY = y + j; // 计算绝对坐标
                if(fixedY < 0 || fixedY >= this.columns) {
                    continue; // 此时越界
                }
                if (!(i == 0 || j == 0)) {
                    continue; // 走直角线路
                }
                // 直角线路的周边格子
                Cell cell = this.cells[fixedX][fixedY];
                if(!cell.isChecked() && cell.isEmpty()){
                    // 记忆化，避免重复检查
                    // 如果是空的，那么继续下一层检查，并打开这个格子，再打开周边
                    cell.setCovered(false);
                    cell.setChecked(true);
                    uncoverAroundCells(fixedX, fixedY);
                    findEmptyCells(fixedX, fixedY, d + 1);
                }
            }
        }

        if(d == 0) {
            // 遍历结束，清空标记
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    this.cells[i][j].setChecked(false);
                }
            }
        }
    }

    /**
     * 打开某一格子周边八个格子。
     *
     * @param x 需要打开的格子坐标
     * @param y 需要打开的格子坐标
     */
    private void uncoverAroundCells(int x, int y) {
        for(int i = -1; i <= 1; i++) {
            int fixedX = x + i; // 计算绝对坐标
            if(fixedX < 0 || fixedX >= this.rows) {
                continue; // 此时越界
            }
            for(int j = -1; j <= 1; j++) {
                int fixedY = y + j; // 计算绝对坐标
                if(fixedY < 0 || fixedY >= this.columns) {
                    continue; // 此时越界
                }
                if (i == 0 && j == 0) {
                    continue;
                }
                Cell cell = this.cells[fixedX][fixedY];
                if (cell.isCovered() && !cell.isEmpty()) {
                    cell.setCovered(false);
                }
            }
        }
    }

    /**
     * 挑选绘制用的图片。
     *
     * @param cell 传入一个格子
     * @return 返回图片编号
     */
    public int pickImageToPaint(@NotNull Cell cell) {
        // 根据我的观察，扫雷中，游戏结束后，只会打开：
        // 已打开的格子、
        // 已标记的格子、
        // 未打开的地雷。

        int type = cell.getValue(); // 默认画周边雷数量的数字

        // 绘制打开与否和标记与否
        if (cell.isMarked()) {
            type = IMAGE_MARKED;
        } else if (cell.isCovered()) {
            type = IMAGE_COVERED;
        }

        if(!inGame) {
            // 游戏结束，绘制地雷
            if (cell.isMine()) {
                type = IMAGE_MINE;
            } else if (cell.isMarked()) {
                // 已经打开的已标记（判断标记正误）
                if (cell.isMine()) {
                    type = IMAGE_MARKED;
                } else {
                    type = IMAGE_MARK_WRONG;
                }
            } // 已打开的维持不变
        }

        return type;
    }

    public void imagesInitialize() {
        imageLabels = new JLabel[rows][columns];
        for(int row = 0; row < this.rows; row++) {
            for(int column = 0; column < this.columns; column++) {
                imageLabels[row][column] = new JLabel();
                imageLabels[row][column].setIcon(imageIcons[IMAGE_COVERED]);
                add(imageLabels[row][column]);
            }
        }

        pictureLabel.setIcon(faceIcons[FACE_GRINNING]);
    }

    public void drawImages() {
        for(int row = 0; row < this.rows; row++) {
            for(int column = 0; column < this.columns; column++) {
                Cell cell = this.cells[row][column];
                int type;
                type = this.pickImageToPaint(cell);
                imageLabels[row][column].setIcon(imageIcons[type]);
            }
        }

        repaint();
    }

    public void redrawImages() {
        drawImages();
    }

    /**
     * 检查游戏状态（处理游戏胜利/失败事件的状态栏变化）。
     */
    public void checkGameStatus() {
        int coveredCells = 0;

        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.columns; j++) {
                Cell cell = this.cells[i][j];
                if(cell.isCovered()) {
                    coveredCells++;
                }
            }
        }

        if (coveredCells == totalMines && inGame) {
            // 全部打开且还在游戏，那么胜利
            inGame = false;
            statusLabel.setText("Won!");
            pictureLabel.setIcon(faceIcons[FACE_SUNGLASSES]);
            Minesweeper.updateTask.disable();
            new GameOverDialog(this, true, Minesweeper.updateTask.getStringToUpdate()).setVisible(true);
        } else if (!inGame) {
            // 不在游戏了，失败
            statusLabel.setText("Lost!");
            pictureLabel.setIcon(faceIcons[FACE_CRYING]);
            Minesweeper.updateTask.disable();
            new GameOverDialog(this, false, Minesweeper.updateTask.getStringToUpdate()).setVisible(true);
        }
    }

    /**
     * 重新开始同一局游戏。
     */
    public void restartGame() {
        UpdateTask.clear();
        pictureLabel.setIcon(faceIcons[FACE_GRINNING]);
        Minesweeper.updateTask.disable();
        Minesweeper.updateTask.manualUpdate();
        this.minesRemaining = totalMines;

        // 打印剩余雷数
        this.statusLabel.setText(Integer.toString(this.minesRemaining));

        this.isFirstPress = true;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                this.cells[i][j].setCovered(true);
                this.cells[i][j].setMarked(false);
            }
        }

        this.inGame = true;
    }

    public void clearCells() {
        UpdateTask.clear();
        Minesweeper.updateTask.manualUpdate();
        this.isFirstPress = true;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                this.cells[i][j].setCovered(true);
                this.cells[i][j].setMarked(false);
                this.cells[i][j].setMine(false);
                this.cells[i][j].setValue(0);
                this.cells[i][j].setChecked(false);
            }
        }

        this.inGame = true;
    }

    /**
     * 开始新一局游戏。
     */
    public void setNewGame() {
        clearCells();

        this.minesRemaining = totalMines;
        pictureLabel.setIcon(faceIcons[FACE_GRINNING]);
        Minesweeper.updateTask.disable();

        // 打印剩余雷数
        this.statusLabel.setText(Integer.toString(this.minesRemaining));

        // 随机生成地雷
        int minesToGenerate = totalMines;
        while(minesToGenerate > 0) {
            int randX = random.nextInt(this.rows);
            int randY = random.nextInt(this.columns);
            Cell cell = this.cells[randX][randY];
            if (!cell.isMine()) {
                cell.setMine(true);
                minesToGenerate--;
            }
        }

        // 计算格子周围雷数
        this.setCellValues();
    }

    public void gameInitialize() {
        this.minesRemaining = totalMines;
        this.inGame = true;
        this.isFirstPress = true;
        UpdateTask.clear();
        Minesweeper.updateTask.manualUpdate();

        // 格子的初始化
        this.cells = new Cell[this.rows][this.columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++) {
                this.cells[i][j] = new Cell();
            }
        }

        // 打印剩余雷数
        this.statusLabel.setText(Integer.toString(this.minesRemaining));

        // 随机生成地雷
        int minesToGenerate = totalMines;
        while(minesToGenerate > 0) {
            int randX = random.nextInt(this.rows);
            int randY = random.nextInt(this.columns);
            Cell cell = this.cells[randX][randY];
            if (!cell.isMine()) {
                cell.setMine(true);
                minesToGenerate--;
            }
        }

        // 计算格子周围雷数
        this.setCellValues();

        imagesInitialize();
        drawImages();
    }

    public int getTotalMines() {
        return totalMines;
    }

    public void setTotalMines(int totalMines) {
        this.totalMines = totalMines;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public boolean isInGame() {
        return inGame;
    }

    class MouseEventHandler extends MouseAdapter {
        /**
         * 重写鼠标事件。
         * @param e 鼠标事件
         */
        @Override
        public void mousePressed(@NotNull MouseEvent e) {
            if(isFirstPress) {
                UpdateTask.clear();
                Minesweeper.updateTask.enable();
                isFirstPress = false;
            }

            int pressedCol = e.getX() / CELL_SIZE;
            int pressedRow = e.getY() / CELL_SIZE;

            if (pressedCol < 0 || pressedCol >= columns || pressedRow < 0 || pressedRow >= rows) {
                return; // 排除越界情况
            }

            if(!inGame){
                return;
            }

            pictureLabel.setIcon(faceIcons[FACE_HUSHED]);

            Cell cellPressed = cells[pressedRow][pressedCol];

            if(e.getButton() == MouseEvent.BUTTON3) {
                // 第三按键也就是右键，处理标记事件
                if (!cellPressed.isCovered()) {
                    return; // 如果已经打开了当然不用处理了
                }

                if (!cellPressed.isMarked()) {
                    // 如果还没标记，则标记（剩余雷数更新）
                    cellPressed.setMarked(true);
                    minesRemaining--;
                } else {
                    // 如果已经标记，则释放（剩余雷数更新）
                    cellPressed.setMarked(false);
                    minesRemaining++;
                }

                statusLabel.setText(Integer.toString(minesRemaining));
                redrawImages();
                checkGameStatus();
            } else if(e.getButton() == MouseEvent.BUTTON1) {
                if (cellPressed.isMarked() || !cellPressed.isCovered()) {
                    // 被标记则不处理
                    return;
                }

                // 左键打开格子
                cellPressed.setCovered(false);

                if (cellPressed.isMine()) {
                    // 触雷游戏结束
                    inGame = false;
                } else if (cellPressed.isEmpty()) {
                    // 打开了 0 格子，那么搜索并打开格子们
                    // // 非 0 格子是不需要递归打开的
                    findEmptyCells(pressedRow, pressedCol, 0);
                }

                redrawImages();
                checkGameStatus();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(!inGame) {
                return;
            }

            pictureLabel.setIcon(faceIcons[FACE_GRINNING]);
        }
    }
}