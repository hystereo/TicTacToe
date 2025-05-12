import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.Arrays;

public class BoardUpdaterRunnable implements Runnable {
    private TicTacToeServer server;
    private BoardUpdater updater;
    private int [][] lastBoard;

    public BoardUpdaterRunnable(TicTacToeServer server, BoardUpdater updater) {
        this.server = server;
        this.updater = updater;
        try{
            this.lastBoard = server.getBoard();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                int[][] board = server.getBoard();
                if(!Arrays.deepEquals(board,lastBoard)){
                    lastBoard = board;
                    updater.updateBoard(board);
                }
                Thread.sleep(100); // Aktualizuj planszę co sekundę (możesz dostosować częstotliwość)
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
