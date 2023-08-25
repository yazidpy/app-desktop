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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.sql.Blob;
/**
 *
 * @author yazid
 */
import java.sql.PreparedStatement;

public class Ajouter_voiture extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Voitures v1 = new Voitures();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    String path = null;
    byte[] voiture_image = null;
    private Voitures instance;

    /**
     * Creates new form Ajouter_voiture
     */
    public Ajouter_voiture(Voitures instance) {
        initComponents();
        this.instance = instance;
        image_voiture.addMouseListener(new java.awt.event.MouseAdapter() { // lorsque on clique sur le jlabel 
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
                    ImageIcon imageic = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(450, 300, Image.SCALE_DEFAULT));
                    // 
                    image_voiture.setIcon(imageic);
                    File image = new File(path);
                    FileInputStream fis = new FileInputStream(image);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    for (int i; (i = fis.read(buff)) != -1;) {
                        bos.write(buff, 0, i);
                    }
                    voiture_image = bos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean valider_formulaire() {
        boolean valide = true;
        if (modele.getText().isEmpty() || annee.getYear()==0 || matricule.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        } else {
                if (!c1.est_chiffre(matricule.getText()) || matricule.getText().length() != 11) {
                    JOptionPane.showMessageDialog(null, "La matricule doit être 11 chiffres !", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;     
            }
        }
        return valide;
    }

    public void inserer_vehicule() {

        if (valider_formulaire() == true) {
            int anne_e = annee.getYear();
            String model = modele.getText();
            String carburant = carbu.getSelectedItem().toString();
            String marque_ = marque.getSelectedItem().toString();
            String etats = etat.getSelectedItem().toString();
            String mat = matricule.getText();
            String transm = transmission.getSelectedItem().toString();
            String type = type_vehicule.getSelectedItem().toString();
            try {
                con = c1.connection_db();
                String sql = "insert into vehicules (marque,année,modèle,carburant,etat,photo,matricule,type_transmission,type_vehicule) values (?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, marque_);
                st.setInt(2, anne_e);
                st.setString(3, model);
                st.setString(4, carburant);
                st.setString(5, etats);
                st.setBytes(6, voiture_image);
                st.setString(7, mat);
                st.setString(8, transm);
                st.setString(9, type);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Vehicule ajouté avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                instance.dispose();
                Voitures v2 = new Voitures();
                v2.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);

            }
        }

    }

    public void effacer_form() {
        
        modele.setText("");
        etat.setSelectedItem("");
        image_voiture.setIcon(null);
        carbu.setSelectedItem("");
        marque.setSelectedItem("");
        matricule.setText("");
    }

    public void affichage(String ids) {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from vehicules where id_vehicule=?");
            st.setString(1, ids);
            rs = st.executeQuery();
            while (rs.next()) {
                annee.setYear(rs.getInt("année"));
                modele.setText(rs.getString("Modèle").trim());
                marque.setSelectedItem(rs.getString("marque").trim());
                etat.setSelectedItem(rs.getString("etat").trim());
                carbu.setSelectedItem(rs.getString("carburant").trim());
                matricule.setText(rs.getString("matricule"));
                transmission.setSelectedItem(rs.getString("type_transmission").trim());
                type_vehicule.setSelectedItem(rs.getString("type_vehicule"));
                Blob blob1 = rs.getBlob("photo");
                byte[] imagebyte = blob1.getBytes(1, (int) blob1.length());
                ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(450, 300, Image.SCALE_DEFAULT));
                image_voiture.setIcon(imag);
                vehicule_n.setText("Véhicule N°: ");
                id_vehicule.setText(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifier_vehicule() {
        try {
            if (valider_formulaire() == true) {
                int anne_e = annee.getYear();
                String model = modele.getText();
                String carburant = carbu.getSelectedItem().toString();
                String marque_ = marque.getSelectedItem().toString();
                String etats = etat.getSelectedItem().toString();
                String mat = matricule.getText();
                String transm = transmission.getSelectedItem().toString();
                String type = type_vehicule.getSelectedItem().toString();
                String update_vehicule = "update vehicules set marque=?,année=?,Modèle=?,carburant=?,etat=?,matricule=?,type_transmission=?,type_vehicule=? where id_vehicule=?";
                PreparedStatement st = con.prepareStatement(update_vehicule);
                st.setString(1, marque_);
                st.setInt(2, anne_e);
                st.setString(3, model);
                st.setString(4, carburant);
                st.setString(5, etats);
                st.setString(6, mat);
                st.setString(7, transm);
                st.setString(8, type);
                st.setString(9, id_vehicule.getText());
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Vehicule modifié avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                instance.dispose();
                Voitures v2 = new Voitures();
                v2.setVisible(true);
                this.dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        etat = new rojerusan.RSComboMetro();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        modele = new app.bolivia.swing.JCTextField();
        marque = new rojerusan.RSComboMetro();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        carbu = new rojerusan.RSComboMetro();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        image_voiture = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        matricule = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();
        transmission = new rojerusan.RSComboMetro();
        jLabel11 = new javax.swing.JLabel();
        type_vehicule = new rojerusan.RSComboMetro();
        jLabel12 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        annee = new rojeru_san.componentes.RSYearDate();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        kGradientPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 102, 153));
        kGradientPanel1.setkGradientFocus(1000);
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        etat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Disponible", "Reservé", "En maintenance" }));
        etat.setColorArrow(new java.awt.Color(0, 102, 153));
        etat.setColorBorde(new java.awt.Color(255, 255, 255));
        etat.setColorFondo(new java.awt.Color(0, 102, 153));
        etat.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Modèle");
        jLabel2.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Année");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Marque");

        modele.setBackground(new java.awt.Color(0, 102, 153));
        modele.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        modele.setForeground(new java.awt.Color(255, 255, 255));
        modele.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        modele.setOpaque(false);
        modele.setPhColor(new java.awt.Color(255, 255, 255));
        modele.setPlaceholder("Modèle ex: 308");
        modele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeleActionPerformed(evt);
            }
        });

        marque.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clio", "Renault", "Peugeot", "Toyota", "Golf", "Audi", "Dacia", "Hyundai" }));
        marque.setColorArrow(new java.awt.Color(0, 102, 153));
        marque.setColorBorde(new java.awt.Color(255, 255, 255));
        marque.setColorFondo(new java.awt.Color(0, 102, 153));
        marque.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Carburant");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Type de transmission");

        carbu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Essence", "Diesel", "Electrique" }));
        carbu.setColorArrow(new java.awt.Color(0, 102, 153));
        carbu.setColorBorde(new java.awt.Color(255, 255, 255));
        carbu.setColorFondo(new java.awt.Color(0, 102, 153));
        carbu.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_voiture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_voiture, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Choisir une Photo");

        matricule.setBackground(new java.awt.Color(0, 102, 153));
        matricule.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        matricule.setForeground(new java.awt.Color(255, 255, 255));
        matricule.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        matricule.setOpaque(false);
        matricule.setPhColor(new java.awt.Color(255, 255, 255));
        matricule.setPlaceholder("Matricule de la voiture");
        matricule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                matriculeActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Matricule");

        transmission.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Automatique", "Manuelle" }));
        transmission.setColorArrow(new java.awt.Color(0, 102, 153));
        transmission.setColorBorde(new java.awt.Color(255, 255, 255));
        transmission.setColorFondo(new java.awt.Color(0, 102, 153));
        transmission.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Type vehicule");

        type_vehicule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Voiture", "Camion" }));
        type_vehicule.setColorArrow(new java.awt.Color(0, 102, 153));
        type_vehicule.setColorBorde(new java.awt.Color(255, 255, 255));
        type_vehicule.setColorFondo(new java.awt.Color(0, 102, 153));
        type_vehicule.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Etat");

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(255, 255, 255));
        rSMaterialButtonCircle1.setBorder(null);
        rSMaterialButtonCircle1.setForeground(new java.awt.Color(0, 102, 153));
        rSMaterialButtonCircle1.setText("Modifier");
        rSMaterialButtonCircle1.setToolTipText("");
        rSMaterialButtonCircle1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSMaterialButtonCircle1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle1MouseClicked(evt);
            }
        });

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 255, 255));
        rSMaterialButtonCircle2.setBorder(null);
        rSMaterialButtonCircle2.setForeground(new java.awt.Color(0, 102, 153));
        rSMaterialButtonCircle2.setText("Ajouter");
        rSMaterialButtonCircle2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSMaterialButtonCircle2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle2MouseClicked(evt);
            }
        });

        vehicule_n.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        vehicule_n.setForeground(new java.awt.Color(255, 255, 255));
        vehicule_n.setToolTipText("");

        id_vehicule.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        id_vehicule.setForeground(new java.awt.Color(255, 255, 255));
        id_vehicule.setToolTipText("");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(196, 196, 196))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel2)
                                        .addComponent(matricule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(etat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                        .addComponent(carbu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9)
                                        .addComponent(modele, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(annee, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)))
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(marque, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel4)
                                    .addComponent(transmission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(type_vehicule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(91, 91, 91))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(vehicule_n)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_vehicule)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addContainerGap())))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(vehicule_n)
                    .addComponent(id_vehicule))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(12, 12, 12)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel2)
                                .addGap(38, 38, 38)
                                .addComponent(modele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addGap(32, 32, 32)
                                .addComponent(annee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88)))
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(30, 30, 30)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(marque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carbu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(28, 28, 28)
                        .addComponent(matricule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(transmission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(type_vehicule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void modeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modeleActionPerformed

    private void matriculeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_matriculeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_matriculeActionPerformed

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        inserer_vehicule();

    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void rSMaterialButtonCircle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1MouseClicked
        // TODO add your handling code here:
        modifier_vehicule();

    }//GEN-LAST:event_rSMaterialButtonCircle1MouseClicked

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
            java.util.logging.Logger.getLogger(Ajouter_voiture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ajouter_voiture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ajouter_voiture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ajouter_voiture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.componentes.RSYearDate annee;
    private rojerusan.RSComboMetro carbu;
    private rojerusan.RSComboMetro etat;
    private javax.swing.JLabel id_vehicule;
    private javax.swing.JLabel image_voiture;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private rojerusan.RSComboMetro marque;
    private app.bolivia.swing.JCTextField matricule;
    private app.bolivia.swing.JCTextField modele;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSComboMetro transmission;
    private rojerusan.RSComboMetro type_vehicule;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables

}
