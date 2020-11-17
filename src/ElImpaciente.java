import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ElImpaciente implements Runnable{
    private final String name;
    private final Phaser phaser;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ElImpaciente(String name, Phaser phaser) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        phaser.register();
        try {
            goToMeeting();
        } catch (InterruptedException e) {
            System.out.printf("%s ha sido interrumpido mientras iba al lugar de salida\n", name);
            return;
        }
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive());
        } catch (InterruptedException e) {
            System.out.printf("%s ha sido interrumpido mientras esperaba a sus colegas en la gasolinera\n", name);
            return;
        }
        try {
            goToVenta();
        } catch (InterruptedException e) {
            System.out.printf("%s ha sido interrumpido mientras iba a la venta desde la gasolinera\n", name);
            return;
        }

        phaser.arriveAndDeregister();

        try{
            goBack();
        } catch (InterruptedException e) {
            System.out.printf("%s ha sido interrumpido mientras volvía a la gasolinera desde la venta\n", name);
            return;
        }
        try {
            goHome();
        } catch (InterruptedException e) {
            System.out.printf("%s ha sido interrumpido volviendo a casa\n", name);
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
