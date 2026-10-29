import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

public class RegistroObjetoView extends JPanel {
    private JTextField txtNombre = new JTextField();
    private JTextField txtDescripcion = new JTextField();
    private JTextField txtLugar = new JTextField();
    private JTextField txtFecha = new JTextField(LocalDate.now().toString());
    private JButton btnRegistrar = new JButton("Registrar objeto");

    public RegistroObjetoView() {
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new GridLayout(5, 2, 5, 5));
        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Descripción:")); add(txtDescripcion);
        add(new JLabel("Lugar:")); add(txtLugar);
        add(new JLabel("Fecha (AAAA-MM-DD):")); add(txtFecha);
        add(new JLabel()); add(btnRegistrar);
    }

    public String getNombreObjeto() { return txtNombre.getText().trim(); }
    public String getDescripcion() { return txtDescripcion.getText().trim(); }
    public String getLugar() { return txtLugar.getText().trim(); }
    public String getFecha() { return txtFecha.getText().trim(); }
    public void agregarRegistroListener(ActionListener listener) { btnRegistrar.addActionListener(listener); }
    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void limpiarCampos() {
        txtNombre.setText(""); txtDescripcion.setText(""); txtLugar.setText("");
        txtFecha.setText(LocalDate.now().toString());
    }
}
