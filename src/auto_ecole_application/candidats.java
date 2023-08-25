/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 *
 * @author yazid
 */
public class candidats extends javax.swing.JFrame {

    public Connection connection_db() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost/auto_ecole", "root", "");
        return con;
    }
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    String path = null;
    byte[] userimage = null;
    DefaultTableModel model;

    /**
     * Creates new form candidats
     */
    public candidats() {
        LocalDate date_de_jour = LocalDate.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_actuell_maintenant = date_de_jour.format(formater);
        String day = date_de_jour.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        initComponents();
        date_actuel_var.setText(date_actuell_maintenant);
        jour_semaine.setText(day);
        selectionner_candidats();
        ajouter_image.addMouseListener(new java.awt.event.MouseAdapter() { // lorsque on clique sur le jlabel 
            public void mouseClicked(java.awt.event.MouseEvent evt) { //à chaque fois qu'on clique il va executer la méthode suivante
                image1MouseClicked(evt);
            }

            private void image1MouseClicked(MouseEvent evt) {
                JFileChooser pic = new JFileChooser(); //la fênetre de choix 
                pic.showOpenDialog(null); //on va ouvrir une autre fênetre à l'intérieur 
                File picture = pic.getSelectedFile();// le fichier(photo) selectionné
                path = picture.getAbsolutePath();//importer 
                BufferedImage img;// on utilise une autre bibliothéque 
                try {
                    img = ImageIO.read(pic.getSelectedFile());//image IO peut lire l'image img ,lire le fichier importé
                    ImageIcon imageic = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
                    // 
                    ajouter_image.setIcon(imageic);
                    File image = new File(path);
                    FileInputStream fis = new FileInputStream(image);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    for (int i; (i = fis.read(buff)) != -1;) {
                        bos.write(buff, 0, i);
                    }
                    userimage = bos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void insertion_candidat() {
        if (valider_formulaire() == true) {
            try {
                String nom = input_nom.getText();
                String prenom = input_prenom.getText();
                String adresse = input_adresse.getText();
                String identite = inpute_carte.getText();
                String telephone = pays.getSelectedItem().toString() + input_tele.getText();
                String sexe_can = sexe_variable.getSelectedItem().toString();
                String categories = categorie.getSelectedItem().toString();
                String email_adr = input_email.getText();
                java.util.Date date_naissance = naissance.getDatoFecha();
                Long date_date = date_naissance.getTime();
                java.sql.Date date_nais = new java.sql.Date(date_date);
                con = connection_db();
                String sql = "insert into candidats (nom,prenom,date_naissance,adresse,telephone,email,categorie,photo,sexe,carte_identite) values (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, nom);
                st.setString(2, prenom);
                st.setDate(3, date_nais);
                st.setString(4, adresse);
                st.setString(5, telephone);
                st.setString(6, email_adr);
                st.setString(7, categories);
                st.setBytes(8, userimage);
                st.setString(9, sexe_can);
                st.setString(10, identite);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Candidat ajouté avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                effacer_formulaire();
                Object[] ligne_vide = {};
                model.addRow(ligne_vide);
                mise_à_jour(model.getRowCount());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean valider_formulaire() {
        boolean valide = true;
        try {
            java.util.Date date_naissance = naissance.getDatoFecha();
            Long date_date = date_naissance.getTime();
            java.sql.Date date_nais = new java.sql.Date(date_date);
            if (input_nom.getText().isEmpty() || input_prenom.getText().isEmpty() || input_tele.getText().isEmpty() || input_email.getText().isEmpty()
                    || input_adresse.getText().isEmpty() || date_nais == null || inpute_carte.getText().isEmpty() || input_tele.getText().isEmpty()) {
                valide = false;
                JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs ! ", null, JOptionPane.ERROR_MESSAGE);
            } else {
                if (!input_email.getText().matches("^.+@.+\\..+$")) {
                    JOptionPane.showMessageDialog(null, "L'email n'est pas valide !", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                }
                if (inpute_carte.getText().length() != 9 || est_chiffre(inpute_carte.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "Numéro de la carte identité est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                }
                if (input_tele.getText().length() != 9 || est_chiffre(input_tele.getText()) == false) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "Numéro de  téléphone est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                }
                if (calculer_age(date_nais) < 18) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "L'âge du candidat doit être superieur à 18   ! ", null, JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur  : " + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        }
        return valide;
    }

    public void effacer_formulaire() {
        input_nom.setText("");
        input_prenom.setText("");
        input_email.setText("");
        input_tele.setText("");
        inpute_carte.setText("");
        input_adresse.setText("");
        naissance.setDatoFecha(null);
        ajouter_image.setIcon(null);
        id_candidat_valeur.setText("");
        id_candidat_text.setText("");
    }

    public void selectionner_candidats() {
        try {
            con = connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidats");
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_candidat");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String Date = rs.getString("Date_de_creation").substring(0, 16);
                Object[] obj = {id, nom, prenom, Date};
                model = (DefaultTableModel) tableau_candidat.getModel();
                model.addRow(obj);
                tableau_candidat.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        table1MouseReleased(evt);
                    }

                    private void table1MouseReleased(MouseEvent evt) {
                        int selectionner = tableau_candidat.getSelectedRow();
                        DefaultTableModel model = (DefaultTableModel) tableau_candidat.getModel();
                        id_candidat_text.setText("id candidat");
                        id_candidat_valeur.setText(model.getValueAt(selectionner, 0).toString());
                        try {
                            con = connection_db();
                            PreparedStatement st = con.prepareStatement("select * from candidats where id_candidat=?");
                            st.setString(1, model.getValueAt(selectionner, 0).toString());
                            rs = st.executeQuery();
                            while (rs.next()) {
                                String nom = rs.getString("nom");
                                String prenom = rs.getString("prenom");
                                String indice_pays = rs.getString("telephone");
                                String ind_pays = indice_pays.substring(1, 3);
                                String telephone = rs.getString("telephone");
                                String tele = telephone.substring(3, 12);
                                Date date_naissance = rs.getDate("date_naissance");
                                String adresse = rs.getString("adresse");
                                String email = rs.getString("email");
                                String cat = rs.getString("categorie");
                                String sexe = rs.getString("sexe");
                                String carte = rs.getString("carte_identite");
                                Blob blob1 = rs.getBlob("photo");
                                byte[] imagebyte = blob1.getBytes(1, (int) blob1.length());
                                ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
                                ajouter_image.setIcon(imag);
                                input_nom.setText(nom);
                                input_prenom.setText(prenom);
                                input_adresse.setText(adresse);
                                inpute_carte.setText(carte);
                                input_tele.setText(tele);
                                input_email.setText(email);
                                naissance.setDatoFecha(date_naissance);
                                pays.setSelectedItem(ind_pays.trim());
                                categorie.setSelectedItem(rs.getString("categorie").trim());
                                sexe_variable.setSelectedItem(rs.getString("sexe").trim());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifier_candidat() {
        if (valider_formulaire() == true) {
            try {
                String nom = input_nom.getText();
                String prenom = input_prenom.getText();
                String adresse = input_adresse.getText();
                String identite = inpute_carte.getText();
                String telephone = pays.getSelectedItem().toString() + input_tele.getText();
                String sexe_can = sexe_variable.getSelectedItem().toString();
                String categories = categorie.getSelectedItem().toString();
                String email_adr = input_email.getText();
                java.util.Date date_naissance = naissance.getDatoFecha();
                Long date_date = date_naissance.getTime();
                java.sql.Date date_nais = new java.sql.Date(date_date);
                String id = id_candidat_valeur.getText();
                String update = "update candidats set nom=?,prenom=?,date_naissance=?,adresse=?,telephone=?,email=?,categorie=?,sexe=?,carte_identite=? where id_candidat=?";
                PreparedStatement st = con.prepareStatement(update);
                st.setString(1, nom);
                st.setString(2, prenom);
                st.setDate(3, date_nais);
                st.setString(4, adresse);
                st.setString(5, telephone);
                st.setString(6, email_adr);
                st.setString(7, categories);
                st.setString(8, sexe_can);
                st.setString(9, identite);
                st.setString(10, id);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Candidat modifié avec succès ", null, JOptionPane.INFORMATION_MESSAGE);
                effacer_formulaire();
                mise_à_jour(model.getRowCount());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void supprimer_candidat() {
        String id = id_candidat_valeur.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String delete = "delete from candidats where id_candidat=?";
                PreparedStatement st = con.prepareStatement(delete);
                st.setString(1, id);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Candidat supprimé avec succès ", null, JOptionPane.INFORMATION_MESSAGE);
                effacer_formulaire();
                model.removeRow(model.getRowCount() - 1);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void mise_à_jour(int rows) {
        String[] ids = new String[rows];
        String[] noms = new String[rows];
        String[] prenoms = new String[rows];
        String[] dates = new String[rows];
        try {
            con = connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidats");
            rs = st.executeQuery();
            int i = 0;
            while (rs.next() && i < rows) {
                ids[i] = rs.getString("id_candidat");
                noms[i] = rs.getString("nom");
                prenoms[i] = rs.getString("prenom");
                dates[i] = rs.getString("date_de_creation").substring(0, 16);
                i++;
            }
            for (int j = 0; j < ids.length; j++) {
                model.setValueAt(ids[j], j, 0);
                model.setValueAt(noms[j], j, 1);
                model.setValueAt(prenoms[j], j, 2);
                model.setValueAt(dates[j], j, 3);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean est_chiffre(String chaine) {
        boolean est_chiifre = true;
        for (int i = 0; i < chaine.length(); i++) {
            if (!Character.isDigit(chaine.charAt(i))) {
                est_chiifre = false;
            }
        }
        return est_chiifre;
    }

    public int calculer_age(java.sql.Date Date_naissance) {
        LocalDate date_naissance = Date_naissance.toLocalDate();
        LocalDate date_actuel = LocalDate.now();
        Period difference = Period.between(date_naissance, date_actuel);
        int age = difference.getYears();
        return age;
    }

    public void rechercher_candidat() {
        String type_recherche = type_de_recherche.getSelectedItem().toString();
        String numero_recherche = input_recherche.getText();
        if (numero_recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez remplir le champs  !", null, JOptionPane.ERROR_MESSAGE);
        } else {
            if (est_chiffre(numero_recherche) == false) {
                JOptionPane.showMessageDialog(null, "Numéro saisie est incorrecte !", null, JOptionPane.ERROR_MESSAGE);
            } else {
                switch (type_recherche) {
                    case "Id candidat":
                        permuter_ligne_tableau(numero_recherche);
                        break;
                    case "Carte identité":
                        if (numero_recherche.length() != 9) {
                            JOptionPane.showMessageDialog(null, "Numéro de la carte identité n'a pas 9 chiffres !", null, JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                }
            }
        }
    }

    public void permuter_ligne_tableau(String numero_recherche) {
        boolean trouvé = false;
        int colonne_id = model.findColumn("id_candidat");
        for (int row = 0; row < model.getRowCount(); row++) {
            Object value_id = model.getValueAt(row, colonne_id);
            Object value_nom = model.getValueAt(row, 1);
            Object value_prenom = model.getValueAt(row, 2);
            Object value_date = model.getValueAt(row, 3);
            if (value_id.equals(numero_recherche)) {
                Object ancien_id = model.getValueAt(0, 0);
                Object ancien_nom = model.getValueAt(0, 1);
                Object ancien_prenom = model.getValueAt(0, 2);
                Object ancien_date = model.getValueAt(0, 3);
                JOptionPane.showMessageDialog(null, "Candidat est trouvé !", null, JOptionPane.INFORMATION_MESSAGE);
                trouvé = true;
                model.setValueAt(value_id, 0, 0);
                model.setValueAt(value_nom, 0, 1);
                model.setValueAt(value_prenom, 0, 2);
                model.setValueAt(value_date, 0, 3);
                model.setValueAt(ancien_id, row, 0);
                model.setValueAt(ancien_nom, row, 1);
                model.setValueAt(ancien_prenom, row, 2);
                model.setValueAt(ancien_date, row, 3);
            }
        }
        if (trouvé == false) {
            JOptionPane.showMessageDialog(null, "Candidat n'est  pas trouvé !", null, JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        date_actuel_var = new javax.swing.JLabel();
        jour_semaine = new javax.swing.JLabel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        inpute_carte = new javax.swing.JTextField();
        input_prenom = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pays = new rojerusan.RSComboMetro();
        naissance = new rojeru_san.componentes.RSDateChooser();
        jLabel10 = new javax.swing.JLabel();
        input_adresse = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        input_nom = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        input_tele = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        input_email = new javax.swing.JTextField();
        categorie = new rojerusan.RSComboMetro();
        jLabel15 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle4 = new rojerusan.RSMaterialButtonCircle();
        panel_photo = new javax.swing.JPanel();
        ajouter_image = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        sexe_variable = new rojerusan.RSComboMetro();
        id_candidat_valeur = new javax.swing.JLabel();
        id_candidat_text = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableau_candidat = new rojerusan.RSTableMetro();
        type_de_recherche = new javax.swing.JComboBox<>();
        input_recherche = new javax.swing.JTextField();
        bouton_recherche = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 3, new java.awt.Color(102, 255, 255)));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setBackground(new java.awt.Color(102, 255, 255));
        jLabel2.setFont(new java.awt.Font("Algerian", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("les candidats");

        jLabel3.setFont(new java.awt.Font("Algerian", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Admin");

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\Auto_Ecole\\icons\\male_user_50px.png")); // NOI18N

        date_actuel_var.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        date_actuel_var.setForeground(new java.awt.Color(255, 255, 255));

        jour_semaine.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jour_semaine.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(247, 247, 247)
                .addComponent(jour_semaine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date_actuel_var)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(date_actuel_var)
                    .addComponent(jour_semaine))
                .addGap(22, 22, 22))
        );

        kGradientPanel1.setkEndColor(new java.awt.Color(51, 255, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(51, 51, 51));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Photo");

        inpute_carte.setBackground(new java.awt.Color(51, 51, 51));
        inpute_carte.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        inpute_carte.setForeground(new java.awt.Color(255, 255, 255));
        inpute_carte.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        inpute_carte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inpute_carteActionPerformed(evt);
            }
        });

        input_prenom.setBackground(new java.awt.Color(51, 51, 51));
        input_prenom.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        input_prenom.setForeground(new java.awt.Color(255, 255, 255));
        input_prenom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_prenom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_prenomActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("sexe");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Prénom");

        pays.setBackground(new java.awt.Color(51, 51, 51));
        pays.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "213", "216", "333" }));
        pays.setColorArrow(new java.awt.Color(0, 0, 0));
        pays.setColorBorde(new java.awt.Color(0, 0, 0));
        pays.setColorFondo(new java.awt.Color(0, 0, 0));
        pays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paysActionPerformed(evt);
            }
        });

        naissance.setBackground(new java.awt.Color(204, 204, 255));
        naissance.setForeground(new java.awt.Color(255, 204, 204));
        naissance.setColorBackground(new java.awt.Color(51, 51, 51));
        naissance.setColorButtonHover(new java.awt.Color(0, 0, 0));
        naissance.setColorDiaActual(new java.awt.Color(255, 102, 255));
        naissance.setColorForeground(new java.awt.Color(0, 0, 0));
        naissance.setColorSelForeground(new java.awt.Color(255, 255, 102));
        naissance.setColorTextDiaActual(new java.awt.Color(51, 51, 51));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Date Naissance");

        input_adresse.setBackground(new java.awt.Color(51, 51, 51));
        input_adresse.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        input_adresse.setForeground(new java.awt.Color(255, 255, 255));
        input_adresse.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_adresse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_adresseActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Numero carte identité");

        input_nom.setBackground(new java.awt.Color(51, 51, 51));
        input_nom.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        input_nom.setForeground(new java.awt.Color(255, 255, 255));
        input_nom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_nomActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Adresse");

        input_tele.setBackground(new java.awt.Color(51, 51, 51));
        input_tele.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        input_tele.setForeground(new java.awt.Color(255, 255, 255));
        input_tele.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_tele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_teleActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Numero Téléphone");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Adresse Email");

        input_email.setBackground(new java.awt.Color(51, 51, 51));
        input_email.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        input_email.setForeground(new java.awt.Color(255, 255, 255));
        input_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_emailActionPerformed(evt);
            }
        });

        categorie.setBackground(new java.awt.Color(0, 0, 0));
        categorie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D" }));
        categorie.setToolTipText("");
        categorie.setColorArrow(new java.awt.Color(0, 0, 0));
        categorie.setColorBorde(new java.awt.Color(0, 0, 0));
        categorie.setColorFondo(new java.awt.Color(0, 0, 0));
        categorie.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Catégorie Permis");

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(0, 204, 51));
        rSMaterialButtonCircle1.setText("effacer");
        rSMaterialButtonCircle1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle1MouseClicked(evt);
            }
        });

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 0, 0));
        rSMaterialButtonCircle2.setText("Supprimer");
        rSMaterialButtonCircle2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle2MouseClicked(evt);
            }
        });

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(51, 51, 51));
        rSMaterialButtonCircle3.setText("ajouter");
        rSMaterialButtonCircle3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle3MouseClicked(evt);
            }
        });

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(51, 51, 255));
        rSMaterialButtonCircle4.setText("modifier");
        rSMaterialButtonCircle4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle4MouseClicked(evt);
            }
        });

        panel_photo.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_photoLayout = new javax.swing.GroupLayout(panel_photo);
        panel_photo.setLayout(panel_photoLayout);
        panel_photoLayout.setHorizontalGroup(
            panel_photoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_photoLayout.createSequentialGroup()
                .addComponent(ajouter_image, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panel_photoLayout.setVerticalGroup(
            panel_photoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ajouter_image, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Nom");

        sexe_variable.setBackground(new java.awt.Color(0, 0, 0));
        sexe_variable.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));
        sexe_variable.setColorArrow(new java.awt.Color(0, 0, 0));
        sexe_variable.setColorBorde(new java.awt.Color(51, 51, 51));
        sexe_variable.setColorFondo(new java.awt.Color(51, 51, 51));

        id_candidat_valeur.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        id_candidat_valeur.setForeground(new java.awt.Color(255, 255, 255));

        id_candidat_text.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        id_candidat_text.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(201, 201, 201))
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(input_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(input_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(id_candidat_text)
                                .addGap(18, 18, 18)
                                .addComponent(id_candidat_valeur)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel_photo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(inpute_carte, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                                        .addComponent(input_email)
                                        .addComponent(categorie, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel11))
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(84, 84, 84)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(naissance, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(88, 88, 88)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel13)
                                                .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                    .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(input_tele, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jLabel8)
                                                .addComponent(sexe_variable, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(rSMaterialButtonCircle4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel15)
                            .addComponent(input_adresse, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(24, Short.MAX_VALUE))))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(input_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(input_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(id_candidat_valeur)
                                .addComponent(id_candidat_text))
                            .addComponent(panel_photo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11)
                .addComponent(input_adresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inpute_carte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(naissance, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addGap(36, 36, 36)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(input_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(input_tele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categorie, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sexe_variable, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 3, 0, 3, new java.awt.Color(0, 153, 153)));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setText("Liste des candidats");

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(252, 252, 252))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 10, 2, new java.awt.Color(0, 153, 153)));

        tableau_candidat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_candidat", "Nom", "Prénom", "Date inscription"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_candidat.setColorBackgoundHead(new java.awt.Color(0, 153, 153));
        tableau_candidat.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_candidat.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorSelBackgound(new java.awt.Color(0, 153, 153));
        tableau_candidat.setFillsViewportHeight(true);
        tableau_candidat.setFocusCycleRoot(true);
        tableau_candidat.setFuenteFilas(new java.awt.Font("Arial", 1, 18)); // NOI18N
        tableau_candidat.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 18)); // NOI18N
        tableau_candidat.setRowHeight(50);
        jScrollPane1.setViewportView(tableau_candidat);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 65, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        type_de_recherche.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        type_de_recherche.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id candidat", "Carte identité" }));

        input_recherche.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        input_recherche.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        input_recherche.setName(""); // NOI18N

        bouton_recherche.setBackground(new java.awt.Color(255, 255, 255));
        bouton_recherche.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        bouton_recherche.setText("Rechercher");
        bouton_recherche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bouton_rechercheMouseClicked(evt);
            }
        });
        bouton_recherche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouton_rechercheActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(type_de_recherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(input_recherche, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bouton_recherche, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(42, 42, 42))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(type_de_recherche, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(input_recherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bouton_recherche)))
                        .addGap(38, 38, 38)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void inpute_carteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inpute_carteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inpute_carteActionPerformed

    private void input_prenomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_prenomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_prenomActionPerformed

    private void input_adresseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_adresseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_adresseActionPerformed

    private void input_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_nomActionPerformed

    private void input_teleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_teleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_teleActionPerformed

    private void input_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_emailActionPerformed

    private void rSMaterialButtonCircle3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3MouseClicked
        // TODO add your handling code here:
        insertion_candidat();
    }//GEN-LAST:event_rSMaterialButtonCircle3MouseClicked

    private void paysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paysActionPerformed

    private void bouton_rechercheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouton_rechercheActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bouton_rechercheActionPerformed

    private void rSMaterialButtonCircle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1MouseClicked
        // TODO add your handling code here:
        effacer_formulaire();
    }//GEN-LAST:event_rSMaterialButtonCircle1MouseClicked

    private void rSMaterialButtonCircle4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4MouseClicked
        // TODO add your handling code here:
        modifier_candidat();
    }//GEN-LAST:event_rSMaterialButtonCircle4MouseClicked

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        supprimer_candidat();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void bouton_rechercheMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bouton_rechercheMouseClicked
        // TODO add your handling code here:
        rechercher_candidat();
    }//GEN-LAST:event_bouton_rechercheMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(candidats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(candidats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(candidats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(candidats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new candidats().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ajouter_image;
    private javax.swing.JButton bouton_recherche;
    private rojerusan.RSComboMetro categorie;
    private javax.swing.JLabel date_actuel_var;
    private javax.swing.JLabel id_candidat_text;
    private javax.swing.JLabel id_candidat_valeur;
    private javax.swing.JTextField input_adresse;
    private javax.swing.JTextField input_email;
    private javax.swing.JTextField input_nom;
    private javax.swing.JTextField input_prenom;
    private javax.swing.JTextField input_recherche;
    private javax.swing.JTextField input_tele;
    private javax.swing.JTextField inpute_carte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jour_semaine;
    private keeptoo.KGradientPanel kGradientPanel1;
    private rojeru_san.componentes.RSDateChooser naissance;
    private javax.swing.JPanel panel_photo;
    private rojerusan.RSComboMetro pays;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private rojerusan.RSComboMetro sexe_variable;
    private rojerusan.RSTableMetro tableau_candidat;
    private javax.swing.JComboBox<String> type_de_recherche;
    // End of variables declaration//GEN-END:variables
}
