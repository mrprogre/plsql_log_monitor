import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import javax.swing.Timer;

public class Main {
    static Timer ClockTimer = new Timer(500, new Clock());
    static String directoryPath = "C:\\Users\\Public\\Documents\\PLSQL_Log_Monitor\\";
    static String favoritesTabPath = directoryPath + "favorites.txt";
    static String logPath = directoryPath + "log.txt";
    public static final Logger LOGGER = Logger.getLogger("");
    //private static final Logger LOGGER = Logger.getLogger(Main.class.getName()); // лог для конкретного класса

    // Создание файлов для избранного и лога
    static {
        File directory = new File(directoryPath);
        File fav_file = new File(favoritesTabPath);
        File log_file = new File(logPath);
        try {
            if (!directory.exists()) directory.mkdirs();
            if (!fav_file.exists()) fav_file.createNewFile();
            if (!log_file.exists()) log_file.createNewFile();

            // запись лога в файл
            Handler handler = new FileHandler(logPath, true);
            handler.setLevel(Level.ALL);
            handler.setEncoding("UTF-8");
            handler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Приложение запущено");
        new Gui();
        ClockTimer.start();
        Common.notification("first you need to connect to VPN");
    }


}
