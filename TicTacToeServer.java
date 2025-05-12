import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeServer extends Remote {
    boolean makeMove(int playerId, int x, int y) throws RemoteException;
    int[][] getBoard() throws RemoteException;
    int checkWinner() throws RemoteException;
    int registerPlayer() throws RemoteException;
}
