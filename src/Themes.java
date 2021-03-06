import java.awt.*;

public class Themes extends Gui{
    static void themeFontColors(String theme) {
        switch (theme) {
            case "Black":
                lblWhere.setForeground(new Color(254, 60, 85));
                JStatement.setForeground(contrastColor);
                JStatement_2.setForeground(contrastColor);
                orderByLbl.setForeground(contrastColor);
                sumLbl.setForeground(new Color(254, 60, 85));
                statusLbl.setForeground(contrastColor);
                analysisLabel.setForeground(new Color(254, 60, 85));
                statusLbl2.setForeground(contrastColor);
                chbxAutoUpdateLbl.setForeground(contrastColor);
                timeLbl.setForeground(contrastColor);
                likeTextField.setBackground(contrastColor);
                textWhereClause.setBackground(contrastColor);
                guiTheme.setBackground(new Color(248, 250, 1));
                color_btn.setBackground(new Color(248, 250, 1));
                comboBoxOrderBy.setBackground(new Color(248, 250, 1));
                comboBoxSorting.setBackground(new Color(248, 250, 1));
                devProd.setBackground(new Color(248, 250, 1));
                passwordField.setBackground(new Color(248, 250, 1));
                break;
            case "Dos":
                lblWhere.setForeground(new Color(254, 60, 85));
                JStatement.setForeground(new Color(152, 219, 255));
                JStatement_2.setForeground(new Color(152, 219, 255));
                orderByLbl.setForeground(new Color(152, 219, 255));
                sumLbl.setForeground(new Color(254, 60, 85));
                analysisLabel.setForeground(new Color(254, 60, 85));
                statusLbl.setForeground(new Color(152, 219, 255));
                statusLbl2.setForeground(new Color(152, 219, 255));
                chbxAutoUpdateLbl.setForeground(new Color(152, 219, 255));
                timeLbl.setForeground(new Color(152, 219, 255));
                likeTextField.setBackground(new Color(173, 170, 173));
                textWhereClause.setBackground(new Color(173, 170, 173));
                guiTheme.setBackground(new Color(5,171,172));
                color_btn.setBackground(new Color(5,171,172));
                comboBoxOrderBy.setBackground(new Color(244, 245, 151));
                comboBoxSorting.setBackground(new Color(244, 245, 151));
                devProd.setBackground(new Color(5,171,172));
                passwordField.setBackground(new Color(173, 170, 173));
                break;
            case "Orange":
            case "Green":
            case "Brown":
            case "Blue":
            case "Pink":
            case "Gray":
            case "Default":
                lblWhere.setForeground(Gui.fontColor);
                JStatement.setForeground(Gui.fontColor);
                JStatement_2.setForeground(Gui.fontColor);
                orderByLbl.setForeground(Gui.fontColor);
                sumLbl.setForeground(new Color(0, 94, 182));
                statusLbl.setForeground(Gui.fontColor);
                statusLbl2.setForeground(Gui.fontColor);
                chbxAutoUpdateLbl.setForeground(Gui.fontColor);
                timeLbl.setForeground(Gui.fontColor);
                analysisLabel.setForeground(Gui.fontColor);
                likeTextField.setBackground(new Color(255, 255, 255));
                textWhereClause.setBackground(new Color(255, 255, 255));
                guiTheme.setBackground(new Color(238, 238, 238));
                color_btn.setBackground(new Color(238, 238, 238));
                comboBoxOrderBy.setBackground(new Color(236, 250, 232));
                comboBoxSorting.setBackground(new Color(236, 250, 232));
                devProd.setBackground(new Color(236, 250, 232));
                passwordField.setBackground(new Color(255, 255, 255));

                break;
        }
    }
}
