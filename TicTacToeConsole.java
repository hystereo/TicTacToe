import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class TicTacToeConsole implements BoardUpdater{
    private TicTacToeServer server;
    private int playerId;

    public TicTacToeConsole(){
        try{
            server = (TicTacToeServer) Naming.lookup("rmi://localhost/TicTacToeServer");
            playerId = server.registerPlayer();
            if(playerId == -1){
                System.out.println("Gra jest już pełna.");
                System.exit(0);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                printBoard(server.getBoard());
                if (playerId == server.checkWinner()) {
                    System.out.println("Gratulacje, wygrałeś!");
                    break;
                } else if (server.checkWinner() == 3) {
                    System.out.println("Remis!");
                    break;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Twój ruch (gracz " + (playerId == 1 ? "X" : "O") + ")");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            try {
                if (server.makeMove(playerId, x, y)) {
                    updateBoard(server.getBoard());
                    if (server.checkWinner() != 0) {
                        printBoard(server.getBoard());
                        System.out.println(server.checkWinner() == 3 ? "Remis!" : "Gracz " + server.checkWinner() + " wygrał!");
                        break;
                    }
                } else {
                    System.out.println("Nieprawidłowy ruch. Spróbuj ponownie.");
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void printBoard(int[][] board) {
        try {
            System.out.println("Aktualna plansza:");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 1) {
                        System.out.print("X ");
                    } else if (board[i][j] == 2) {
                        System.out.print("O ");
                    } else {
                        System.out.print("- ");
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TicTacToeConsole console = new TicTacToeConsole();
                BoardUpdaterRunnable updaterRunnable = new BoardUpdaterRunnable(console.server,console);
                Thread updateThread = new Thread(updaterRunnable);
                updateThread.start();

                console.startGame();
            }
        });
    }

    @Override
    public void updateBoard(int[][] board) throws RemoteException{
            printBoard(board);
    }
}
