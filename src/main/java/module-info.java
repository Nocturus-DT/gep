module teste {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Pacotes acessíveis ao FXMLLoader
    opens br.edu.ifpr.gep.aplicacao to javafx.fxml;
    opens br.edu.ifpr.gep.panel to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados.alterar to javafx.fxml;
    opens br.edu.ifpr.gep.panel.consulta to javafx.fxml;

    // Pacotes exportados para outras partes do projeto
    exports br.edu.ifpr.gep.aplicacao;
    exports br.edu.ifpr.gep.panel;
    exports br.edu.ifpr.gep.panel.dados;
    exports br.edu.ifpr.gep.panel.dados.alterar;
    exports br.edu.ifpr.gep.panel.consulta;
}
