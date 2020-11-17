import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int NORMAL_FRIENDS = 5;

    public static void main(String[] args) throws InterruptedException {
        int i;
        CiclistasPhaser phaser = new CiclistasPhaser();
        for (i = 0; i < NORMAL_FRIENDS; i++) {
            new Thread(new Friend("Friend #" + i, phaser), "Friend #" + i).start();
        }
        // El que no espera
        new Thread(new ElImpaciente("Friend #" + i, phaser), "Friend #" + i).start();
        i++;
        // El tardón que pa cuando llega tos han salío de la gasolinera
        TimeUnit.SECONDS.sleep(4);
        new Thread(new Tardon("Friend #" + i, phaser), "Friend #" + i).start();
        i++;
        // El tardón que pa cuando llega tos han tirao pa casa
        TimeUnit.SECONDS.sleep(22);
        new Thread(new Tardon("Friend #" + i, phaser), "Friend #" + i).start();
        i++;

    }
}
