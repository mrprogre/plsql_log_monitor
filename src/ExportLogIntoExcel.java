import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExportLogIntoExcel {
    public ExportLogIntoExcel() {
    }

    public static void exportToExcel() {
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xls", "*.xls", "*.XLS", "*.*");
            JFileChooser save_to = new JFileChooser();
            save_to.setFileFilter(filter);
            save_to.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
            int ret = save_to.showDialog(null, "Save");
            if (ret == 0) {
                File file = new File(save_to.getSelectedFile() + ".xls");
                WritableWorkbook new_excel = Workbook.createWorkbook(file);
                WritableSheet page = new_excel.createSheet("mrpro", 0);
                page.getSettings().setShowGridLines(true);
                page.setColumnView(0, 10);
                page.setColumnView(1, 26);
                page.setColumnView(2, 200);
                page.setRowView(0, 400);
                WritableFont wf = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                WritableFont wf_bold = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                WritableFont wf_link = new WritableFont(WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
                wf_link.setColour(Colour.DARK_GREEN);
                WritableCellFormat wcf_link = new WritableCellFormat(wf_link);
                wcf_link.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                wcf_link.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_link.setWrap(true);
                new WritableCellFormat(wf);
                WritableCellFormat wcf = new WritableCellFormat(wf);
                wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf.setWrap(true);
                WritableCellFormat wcf_centre_no_bold = new WritableCellFormat(wf);
                wcf_centre_no_bold.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                wcf_centre_no_bold.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_centre_no_bold.setAlignment(Alignment.CENTRE);
                WritableCellFormat wcf_centre = new WritableCellFormat(wf);
                wcf_centre.setAlignment(Alignment.CENTRE);
                wcf_centre.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_centre.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                wcf_centre.setVerticalAlignment(VerticalAlignment.CENTRE);
                WritableCellFormat wcf_centre_bold = new WritableCellFormat(wf_bold);
                wcf_centre_bold.setAlignment(Alignment.CENTRE);
                wcf_centre_bold.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                wcf_centre_bold.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_centre_bold.setBackground(Colour.LIGHT_GREEN);
                String[] headers = new String[]{"Num", "Date", "Text"};

                int z;
                for(z = 0; z < headers.length; ++z) {
                    Label x = new Label(z, 0, headers[z], wcf_centre_bold);
                    page.addCell(x);
                }

                for(z = 0; z < Gui.model.getRowCount(); ++z) {
                    Number y1 = new Number(0, z + 1, Integer.parseInt(Gui.model.getValueAt(z, 0).toString()), wcf_centre_no_bold);
                    Label y2 = new Label(1, z + 1, Gui.model.getValueAt(z, 1).toString(), wcf_centre_no_bold);
                    Label y3 = new Label(2, z + 1, Gui.model.getValueAt(z, 2).toString(), wcf);
                    page.addCell(y1);
                    page.addCell(y2);
                    page.addCell(y3);
                    page.setRowView(z + 1, 600);
                }

                new_excel.write();
                new_excel.close();
                Common.notification("export is done");
            } else {
                Common.notification("export canceled");
            }
        } catch (IOException | WriteException var20) {
            var20.printStackTrace();
            Common.notification("export exception.. please try again");
        }

    }
}