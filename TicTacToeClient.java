import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TicTacToeClient extends JFrame implements BoardUpdater{
    private JButton[][] buttons;
    private TicTacToeServer server;
    private int playerId;
    private TicTacToeConsole console;

    public TicTacToeClient(TicTacToeConsole console){
        this.console = console;
    }
    public TicTacToeClient() {
        // Inicjalizacja GUI
        buttons = new JButton[3][3];
        setLayout(new GridLayout(3, 3));

        // Łączenie z serwerem RMI
        try {
            server = (TicTacToeServer) Naming.lookup("rmi://localhost/TicTacToeServer");
            playerId = server.registerPlayer();
            if (playerId == -1) {
                JOptionPane.showMessageDialog(this, "Gra jest już pełna.");
                System.exit(0);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Tworzenie przycisków planszy
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                final int x = i;
                final int y = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeMove(x, y);

                        if(playerId == 1){

                        }
                    }
                });
                add(buttons[i][j]);
            }
        }

        // Ustawienia okna
        setTitle("Kółko i Krzyżyk");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void makeMove(int x, int y) {
        try {
            if (server.makeMove(playerId, x, y)) {
                int[][] updatedBoard = server.getBoard();
                updateBoard(updatedBoard);
                console.updateBoard(updatedBoard);
                int winner = server.checkWinner();
                if (winner != 0) {
                    JOptionPane.showMessageDialog(this, winner == 3 ? "Remis!" : "Gracz " + winner + " wygrał!");
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBoard(int[][] board) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == 1) {
                            buttons[i][j].setText("X");
                        } else if (board[i][j] == 2) {
                            buttons[i][j].setText("O");
                        } else {
                            buttons[i][j].setText("");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            if(console != null){
                console.updateBoard(board);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TicTacToeClient client = new TicTacToeClient();
                BoardUpdaterRunnable updaterRunnable = new BoardUpdaterRunnable(client.server,client);
                Thread updaterThread = new Thread(updaterRunnable);
                updaterThread.start();
            }
        });
    }
}
