module teste {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens br.edu.ifpr.gep.aplicacao to javafx.fxml;
    opens br.edu.ifpr.gep.panel to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados.alterar to javafx.fxml;
    opens br.edu.ifpr.gep.panel.consulta to javafx.fxml;

    exports br.edu.ifpr.gep.aplicacao;
    exports br.edu.ifpr.gep.panel;
    exports br.edu.ifpr.gep.panel.dados;
    exports br.edu.ifpr.gep.panel.consulta;
}