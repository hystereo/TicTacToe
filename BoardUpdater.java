import java.rmi.RemoteException;

public interface BoardUpdater {
    void updateBoard(int [][] board) throws RemoteException;
}
