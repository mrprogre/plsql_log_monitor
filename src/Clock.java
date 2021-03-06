import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

class Clock implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        /* date
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        */

        // time
        DateFormat df = new SimpleDateFormat("HH:mm:ss"); //hh - 12 часов, HH - 24 часа
        Date time = Calendar.getInstance().getTime();
        String reportDate = df.format(time);
        Gui.timeLbl.setText(reportDate);
    }
}