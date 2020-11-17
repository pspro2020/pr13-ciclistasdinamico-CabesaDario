import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Phaser;

public class CiclistasPhaser extends Phaser {

    public static final int ARRIVE_TO_GAS_STATION = 0;
    public static final int ARRIVE_TO_VENTA = 1;
    public static final int BACK_TO_GAS_STATION = 2;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case ARRIVE_TO_GAS_STATION:
                System.out.printf("%s -> Los %d amigos han llegado a la gasolinera de quedada (executed in %s)\n",
                        LocalTime.now().format(dateTimeFormatter), registeredParties,
                        Thread.currentThread().getName());
                break;
            case ARRIVE_TO_VENTA:
                System.out.printf("%s -> Los %d han llegao a la venta los nogales (executed in %s)\n",
                        LocalTime.now().format(dateTimeFormatter), registeredParties,
                        Thread.currentThread().getName());
                break;
            case BACK_TO_GAS_STATION:
                System.out.printf("%s -> Los %d amigos han vuelto a la gasolinera desde la venta (executed in %s)\n",
                        LocalTime.now().format(dateTimeFormatter), registeredParties,
                        Thread.currentThread().getName());
                // The phaser is marked as terminated.
                return true;
        }
        return super.onAdvance(phase, registeredParties);
    }

}