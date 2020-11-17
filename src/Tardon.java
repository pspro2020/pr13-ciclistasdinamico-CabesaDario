import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Tardon implements Runnable {

    private final String name;
    private final Phaser phaser;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Tardon(String name, Phaser phaser) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        if (!phaser.isTerminated()) {
            int joinPhase = phaser.register();
            System.out.printf("%s -> %s se ha unido a los migos que están en la fase #%d\n",
                    LocalTime.now().format(dateTimeFormatter), name, joinPhase);
            try {
                goToMeeting();
            } catch (InterruptedException e) {
                System.out.printf("%s ha sido interrumpido mientras iba a la venta\n", name);
                return;
            }
            // Tardon comprueba en que fase estan los amigos
            if (joinPhase <= CiclistasPhaser.ARRIVE_TO_GAS_STATION) {
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s ha sido interrumpido mientras esperaba a sus colegas en la gasolinera\n", name);
                    return;
                }
            }
            try {
                goToVenta();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while drinking the first beer\n", name);
                return;
            }
            if (joinPhase <= CiclistasPhaser.ARRIVE_TO_VENTA) {
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s ha sido interrumpido mientras esperaba a sus colegas en la venta\n", name);
                    return;
                }
            }
            try {
                goBack();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while drinking the second beer\n", name);
                return;
            }
            if (joinPhase <= CiclistasPhaser.BACK_TO_GAS_STATION) {
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s ha sido interrumpido mientras esperaba a sus colegas en la gasoloinera para volver a casa\n", name);
                    return;
                }
            }
            try {
                goHome();
            } catch (InterruptedException e) {
                System.out.printf("%s ha sido interrumpido mientras volvía a casa\n", name);
            }
        } else {
            System.out.printf("%s -> %s llegó demasiado tarde\n",
                    LocalTime.now().format(dateTimeFormatter), name);
        }
    }
    private void goToMeeting() throws InterruptedException {
        System.out.printf("%s -> %s ha salido de casa\n",
                LocalTime.now().format(dateTimeFormatter), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3) + 1);
        System.out.printf("%s -> %s ha llegado a la gasolinera desde su casa\n",
                LocalTime.now().format(dateTimeFormatter), name);
    }

    private void goToVenta() throws InterruptedException {
        System.out.printf("%s -> %s sale hacia la venta\n",
                LocalTime.now().format(dateTimeFormatter), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6) + 5);
        System.out.printf("%s -> %s ha legado a la venta\n",
                LocalTime.now().format(dateTimeFormatter), name);
    }

    private void goBack() throws InterruptedException {
        System.out.printf("%s -> %s sale de la venta\n",
                LocalTime.now().format(dateTimeFormatter), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6) + 5);
        System.out.printf("%s -> %s ha vuelto a la gasolinera desde la venta\n",
                LocalTime.now().format(dateTimeFormatter), name);
    }

    private void goHome() throws InterruptedException {
        System.out.printf("%s -> %s va pa su casa\n",
                LocalTime.now().format(dateTimeFormatter), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3) + 1);
        System.out.printf("%s -> %s ya en casa\n",
                LocalTime.now().format(dateTimeFormatter), name);
    }
}
