import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelFiltrosAvanzados extends JPanel {

    private final JTextField txtFechaInicial = new JTextField(10);
    private final JTextField txtFechaFinal = new JTextField(10);
    private final JTextField txtPalabrasClave = new JTextField(18);

    private final JButton btnAplicar = new JButton("Aplicar filtros");
    private final JButton btnLimpiar = new JButton("Limpiar filtros");

    public PanelFiltrosAvanzados() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Filtros avanzados"));

        JPanel campos = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel fechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechas.add(new JLabel("Fecha inicial:"));
        fechas.add(txtFechaInicial);
        fechas.add(new JLabel("Fecha final:"));
        fechas.add(txtFechaFinal);

        JPanel palabras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        palabras.add(new JLabel("Palabras clave:"));
        palabras.add(txtPalabrasClave);

        campos.add(fechas);
        campos.add(palabras);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(btnLimpiar);
        acciones.add(btnAplicar);

        add(campos, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

        EstiloUVG.aplicar(this);
    }

    public String getFechaInicial() {
        return txtFechaInicial.getText().trim();
    }

    public String getFechaFinal() {
        return txtFechaFinal.getText().trim();
    }

    public String getPalabrasClave() {
        return txtPalabrasClave.getText().trim();
    }

    public void agregarAplicarListener(ActionListener listener) {
        btnAplicar.addActionListener(listener);
    }

    public void agregarLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void limpiarCampos() {
        txtFechaInicial.setText("");
        txtFechaFinal.setText("");
        txtPalabrasClave.setText("");
    }
}
