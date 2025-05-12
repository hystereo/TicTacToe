import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class TicTacToeServerImpl extends UnicastRemoteObject implements TicTacToeServer {
    private int[][] board;
    private int currentPlayer;
    private int playerCount;

    public TicTacToeServerImpl() throws RemoteException {
        board = new int[3][3];
        currentPlayer = 1;
        playerCount = 0;
    }

    @Override
    public synchronized int registerPlayer() throws RemoteException {
        if (playerCount < 2) {
            playerCount++;
            return playerCount;
        } else {
            return -1; // Game is full
        }
    }

    @Override
    public synchronized boolean makeMove(int playerId, int x, int y) throws RemoteException {
        if (board[x][y] == 0 && playerId == currentPlayer) {
            board[x][y] = playerId;
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            return true;
        }
        return false;
    }

    @Override
    public synchronized int[][] getBoard() throws RemoteException {
        return board;
    }

    @Override
    public synchronized int checkWinner() throws RemoteException {
        // Sprawdź wiersze
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }
        // Sprawdź kolumny
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        // Sprawdź przekątne
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        // Sprawdź remis
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return 0;
                }
            }
        }
        return 3; // Remis
    }

    public static void main(String[] args) {
        try {
            TicTacToeServer server = new TicTacToeServerImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("TicTacToeServer", server);
            System.out.println("TicTacToeServer is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
