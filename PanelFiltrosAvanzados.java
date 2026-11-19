import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.CENTER;

        JPanel fechas = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        fechas.add(new JLabel("Fecha inicial (AAAA-MM-DD):"));
        fechas.add(txtFechaInicial);
        fechas.add(new JLabel("Fecha final (AAAA-MM-DD):"));
        fechas.add(txtFechaFinal);

        JPanel palabras = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        palabras.add(new JLabel("Palabras clave:"));
        palabras.add(txtPalabrasClave);

        c.gridx = 0;
        c.gridy = 0;
        campos.add(fechas, c);
        c.gridy = 1;
        campos.add(palabras, c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        acciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
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
