package gui;

import field.FieldSize;
import field.Game;
import field.GameResult;
import squares.Square;
import utils.ForEachSquare;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FieldDisplay extends JFrame{
    final private Game game;
    private JPanel fieldContainer;
    private JPanel minesCountContainer;

    public FieldDisplay(Game game) {
        this.game = game;

        // main frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Minesweeper");
        this.setMinimumSize(new Dimension(520, 640));
        this.getContentPane().setBackground(new Color(0xF4F4FB));
        this.setVisible(true);

        this.createTopBar();
        this.createField();

        this.pack();
    }

    // top bar
    private void createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setPreferredSize(new Dimension(40, 60));
        topBar.setBackground(new Color(0x484e92));
        topBar.setVisible(true);

        this.fieldSizeSelectionContainer(topBar);
        this.displayMinesCount(topBar);

        this.add(topBar, BorderLayout.NORTH);
    }

    private String getFieldSizeText () {
        String fieldSizeStr;
        switch (this.game.fieldSize) {
            case FieldSize.medium -> fieldSizeStr = "Medium";
            case FieldSize.large -> fieldSizeStr = "Large";
            default -> fieldSizeStr = "Small";
        }

        return "Select field size (Current: " + fieldSizeStr + ")";
    }
    private void fieldSizeSelectionContainer(Container topBar) {
        JButton selectButton = new JButton(this.getFieldSizeText());
        JPopupMenu dropdownMenu = new JPopupMenu();

        // options
        JMenuItem smallFieldOption = new JMenuItem("Small Field");
        JMenuItem mediumFieldOption = new JMenuItem("Medium Field");
        JMenuItem largeFieldOption = new JMenuItem("Large Field");

        // click handlers
        smallFieldOption.addActionListener(_ -> {
            this.game.createField(FieldSize.small);
            selectButton.setText(getFieldSizeText());
            rerender();
        });
        mediumFieldOption.addActionListener(_ -> {
            this.game.createField(FieldSize.medium);
            selectButton.setText(getFieldSizeText());
            rerender();
        });
        largeFieldOption.addActionListener(_ -> {
            this.game.createField(FieldSize.large);
            selectButton.setText(getFieldSizeText());
            rerender();
        });

        dropdownMenu.add(smallFieldOption);
        dropdownMenu.add(mediumFieldOption);
        dropdownMenu.add(largeFieldOption);

        selectButton.addActionListener(_ -> {
            // Show the dropdown menu below the button
            dropdownMenu.show(selectButton, 0, selectButton.getHeight());
        });

        topBar.add(selectButton, BorderLayout.CENTER);
    }

    private void displayMinesCount(Container topBar) {
        this.minesCountContainer = new JPanel();
        JLabel minesLeft = new JLabel(Integer.toString((game.minesLeft)));

        this.minesCountContainer.add(minesLeft);
        topBar.add(this.minesCountContainer);
    }

    // field
    private void createField() {
        this.fieldContainer = new JPanel();
        this.fieldContainer.setVisible(true);

        ScalablePanel field = new ScalablePanelBuilder()
            .setMinSizes(this.game.columns * 20, this.game.rows * 20)
            .setScalePercentage(0.8, 0.8)
            .setMaxSizes(this.game.columns * 24, this.game.rows * 24)
            .build();
        field.setLayout(new GridLayout(this.game.rows, this.game.columns));
        field.setBackground(new Color(0x484e92));
        field.setVisible(true);

        // fill buttons
        ForEachSquare.loop(this.game, (int row, int col) -> this.createSquare(field, row, col));

        this.fieldContainer.add(field);
        this.add(this.fieldContainer, BorderLayout.CENTER);
    }

    private void createSquare(Container container, int row, int column) {
        SquareColor color = (row + column) % 2 == 0 ? SquareColor.light : SquareColor.dark;
        int uncheckedColor = color == SquareColor.light ? 0xaad751 : 0x85ad39;

        Square square = this.game.field[row][column];

        // for checked squares
        if(square.isRevealed) {
            this.openSquare(container, row, column);
            return;
        }

        // for unchecked squares
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(20, 20));
        button.setBackground(new Color(uncheckedColor));
        button.setBorderPainted(false);

        // if square is flagged
        if(square.isFlagged) {
            ImageIcon flagImage = new ImageIcon("src/assets/flag.png");
            Image resizedImage = flagImage.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

            button.setIcon(new ImageIcon(resizedImage));
        }

        // handle click
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // marked squares should not be opened by clicking
                    if(square.isFlagged) return;

                    container.remove(button);
                    openSquare(container, row, column);

                    square.open(game);

                    // rerender the field to correctly display the field if multiple squares were opened
                    rerender();

                    // if the game ended => show the modal
                    if(!game.isGameInProgress) {
                        // if no unrevealed non mine squares remain => player wins
                        GameResult result = game.squaresRemain == 0 ? GameResult.win : GameResult.lose;
                        endModal(result);
                    }
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    square.toggleFlag(game);
                    rerender();
                }
            }
        });

        container.add(button);
    }

    private void openSquare(Container container, int row, int column) {
        // define color for checked square
        SquareColor color = (row + column) % 2 == 0 ? SquareColor.light : SquareColor.dark;
        int checkedColor = color == SquareColor.light ? 0xe5c29f : 0xd7b899;

        Square square = this.game.field[row][column];

        JPanel squareContainer = new JPanel();
        squareContainer.setPreferredSize(new Dimension(20, 20));
        squareContainer.setBackground(new Color(checkedColor));
        squareContainer.setVisible(true);

        // display content inside the square
        this.addRevealedSquareContent(squareContainer, square);

        // add square at the same position
        container.add(squareContainer, row * this.game.columns + column);

        // update the layout
        container.revalidate();
        container.repaint();
    }

    private void addRevealedSquareContent(JPanel squareContainer, Square square) {
        if(square.isMine) {
            ImageIcon mineImage = new ImageIcon("src/assets/mine.png");
            Image resizedImage = mineImage.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JLabel mineLabel = new JLabel(new ImageIcon(resizedImage));

            squareContainer.add(mineLabel, BorderLayout.CENTER);
            return;
        }

        // if square has no adj mines, then it should be empty
        int adjMinesNum = square.adjacentMines;
        if(adjMinesNum == 0) {
            return;
        }

        JLabel adjMines = new JLabel(Integer.toString(square.adjacentMines), JLabel.CENTER);
        adjMines.setFont(new Font("Serif", Font.BOLD, 16));
        adjMines.setPreferredSize(new Dimension(16, 16));

        // set unique color for each number
        Color textColor = switch (adjMinesNum) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.CYAN;
            case 5 -> Color.PINK;
            case 6 -> Color.ORANGE;
            case 7 -> Color.MAGENTA;
            default -> Color.LIGHT_GRAY;
        };

        adjMines.setForeground(textColor);
        squareContainer.add(adjMines, BorderLayout.CENTER);
    }

    private void rerender() {
        // rerender field
        this.remove(this.fieldContainer);
        this.createField();

        // rerender the count of unmarked mines
        Container topBar = this.minesCountContainer.getParent();
        topBar.remove(this.minesCountContainer);
        this.displayMinesCount(topBar);

        this.revalidate(); 
        this.repaint();
    }

    private void endModal(GameResult result) {
        String resultTitle = result == GameResult.win ? "(You win)" : "(You lose)";
        JDialog dialog = new JDialog(this, "Game over " + resultTitle, true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new FlowLayout());

        JButton tryAgainButton = new JButton("Try again");
        tryAgainButton.addActionListener(_ -> {
            this.game.createField(game.fieldSize);
            this.rerender();
            
            dialog.dispose();
        });

        JButton closeButton = new JButton("Close the game");
        closeButton.addActionListener(_ -> {
            // close the windows
            this.dispose();
            dialog.dispose();
        });

        dialog.add(tryAgainButton);
        dialog.add(closeButton);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
