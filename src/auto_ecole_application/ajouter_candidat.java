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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author yazid
 */
public class ajouter_candidat extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    String path = null;
    byte[] user_image = null;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    private Candidat instance;
    Acceuil acceuil = new Acceuil();

    /**
     * Creates new form ajouter_candidat
     */
    public ajouter_candidat(Candidat instance) {
        this.instance = instance;
        initComponents();

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
                    user_image = bos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean valider_formulaire() {
        boolean valide = true;
        try {
            java.util.Date date_naissance = naissance.getDatoFecha();
            Long date_date = date_naissance.getTime();
            java.sql.Date date_nais = new java.sql.Date(date_date);
            if (input_nom.getText().isEmpty() || input_prenom.getText().isEmpty() || input_email.getText().isEmpty()
                    || input_adresse.getText().isEmpty() || date_naissance == null || inpute_carte.getText().isEmpty() || input_tele.getText().isEmpty()) {
                valide = false;
                JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs ! ", null, JOptionPane.ERROR_MESSAGE);
            } else {
                if (!input_email.getText().matches("^.+@.+\\..+$")) {
                    JOptionPane.showMessageDialog(null, "L'email n'est pas valide !", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                }
                if (inpute_carte.getText().length() != 9 || c1.est_chiffre(inpute_carte.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "Numéro de la carte identité est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                }
                if (input_tele.getText().length() != 9 || c1.est_chiffre(input_tele.getText()) == false) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "Numéro de  téléphone est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                }
                if (c1.calculer_age(date_nais) < 18) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "L'âge du candidat doit être superieur à 18   ! ", null, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Vous devez choisir la date", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        }
        return valide;
    }

    public void inserer_candidat() {
        if (valider_formulaire() == true) {
            String nom = input_nom.getText();
            String prenom = input_prenom.getText();
            String adresse = input_adresse.getText();
            String telephone = pays.getSelectedItem().toString() + input_tele.getText();
            String emails = input_email.getText();
            String sexes = sexe.getSelectedItem().toString();
            String carte_id = inpute_carte.getText();
            java.util.Date date_naissance = naissance.getDatoFecha();
            Long date_date = date_naissance.getTime();
            java.sql.Date date_nais = new java.sql.Date(date_date);
            try {
                con = c1.connection_db();
                String sql = "insert into candidat (nom,prenom,date_naissance,adresse,telephone,email,photo,sexe,carte_identite) values (?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, nom);
                st.setString(2, prenom);
                st.setDate(3, date_nais);
                st.setString(4, adresse);
                st.setString(5, telephone);
                st.setString(6, emails);
                st.setBytes(7, user_image);
                st.setString(8, sexes);
                st.setString(9, carte_id);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Candidat ajouté avec succès attendez pour l'envoi de l'email  ", null, JOptionPane.INFORMATION_MESSAGE);
                if (acceuil.EmailSender(emails, "Confirmation d'inscription ", "Bonjour,\n Votre Inscription à l'auto école à été bien effectué consulter votre email pour voir votre état d'avancement\nMerci !\n\n Coridalement.") == true) {
                    JOptionPane.showMessageDialog(null,"Email envoyé avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                }
                instance.model.setRowCount(0);
                instance.selectionner_candidats();
                instance.statistique();
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void affichage(String ids) {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidat where id_candidat=?");
            st.setString(1, ids);
            rs = st.executeQuery();
            while (rs.next()) {
                input_nom.setText(rs.getString("nom"));
                input_prenom.setText(rs.getString("prenom"));
                input_adresse.setText(rs.getString("adresse"));
                input_email.setText(rs.getString("email"));
                pays.setSelectedItem(rs.getString("telephone").substring(1, 3).trim());
                input_tele.setText(rs.getString("telephone").substring(3, 12));
                sexe.setSelectedItem(rs.getString("sexe").trim());
                naissance.setDatoFecha(rs.getDate("date_naissance"));
                inpute_carte.setText(rs.getString("carte_identite"));
                Blob blob1 = rs.getBlob("photo");
                byte[] imagebyte = blob1.getBytes(1, (int) blob1.length());
                ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
                ajouter_image.setIcon(imag);
                vehicule_n.setText("Candidat N°: ");
                id_vehicule.setText(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifier_candidat() {
        try {
            if (valider_formulaire() == true) {
                String nom = input_nom.getText();
                String prenom = input_prenom.getText();
                String adresse = input_adresse.getText();
                String telephone = pays.getSelectedItem().toString() + input_tele.getText();
                String emails = input_email.getText();
                String sexes = sexe.getSelectedItem().toString();
                String carte_id = inpute_carte.getText();
                java.util.Date date_naissance = naissance.getDatoFecha();
                Long date_date = date_naissance.getTime();
                java.sql.Date date_nais = new java.sql.Date(date_date);
                String update_vehicule = "update candidat set nom=?,prenom=?,date_naissance=?,adresse=?,telephone=?,email=?,sexe=?,carte_identite=? where id_candidat=?";
                PreparedStatement st = con.prepareStatement(update_vehicule);
                st.setString(1, nom);
                st.setString(2, prenom);
                st.setDate(3, date_nais);
                st.setString(4, adresse);
                st.setString(5, telephone);
                st.setString(6, emails);
                st.setString(7, sexes);
                st.setString(8, carte_id);
                st.setString(9, id_vehicule.getText());
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Profil d'un candidat modifié avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                instance.model.setRowCount(0);
                instance.selectionner_candidats();
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
        input_prenom = new app.bolivia.swing.JCTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        input_nom = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        pays = new rojerusan.RSComboMetro();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ajouter_image = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        naissance = new rojeru_san.componentes.RSDateChooser();
        input_email = new app.bolivia.swing.JCTextField();
        input_adresse = new app.bolivia.swing.JCTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        input_tele = new app.bolivia.swing.JCTextField();
        sexe = new rojerusan.RSComboMetro();
        jLabel15 = new javax.swing.JLabel();
        inpute_carte = new app.bolivia.swing.JCTextField();
        jLabel8 = new javax.swing.JLabel();

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

        input_prenom.setBackground(new java.awt.Color(0, 102, 153));
        input_prenom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_prenom.setForeground(new java.awt.Color(255, 255, 255));
        input_prenom.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_prenom.setOpaque(false);
        input_prenom.setPhColor(new java.awt.Color(255, 255, 255));
        input_prenom.setPlaceholder("Saisir le Prénom du candidat");
        input_prenom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_prenomActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nom");
        jLabel2.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Prénom");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Date naissance");

        input_nom.setBackground(new java.awt.Color(0, 102, 153));
        input_nom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_nom.setForeground(new java.awt.Color(255, 255, 255));
        input_nom.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_nom.setOpaque(false);
        input_nom.setPhColor(new java.awt.Color(255, 255, 255));
        input_nom.setPlaceholder("Saisir le nom du candidat");
        input_nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_nomActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Sexe");

        pays.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "213", "216", "214" }));
        pays.setColorArrow(new java.awt.Color(0, 102, 153));
        pays.setColorBorde(new java.awt.Color(255, 255, 255));
        pays.setColorFondo(new java.awt.Color(0, 102, 153));
        pays.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

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
            .addComponent(ajouter_image, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ajouter_image, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Choisir une Photo");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Adresse");

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

        naissance.setBackground(new java.awt.Color(0, 102, 153));
        naissance.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 1, 2, 1, new java.awt.Color(255, 255, 255)));
        naissance.setForeground(new java.awt.Color(255, 204, 204));
        naissance.setColorBackground(new java.awt.Color(0, 102, 153));
        naissance.setColorButtonHover(new java.awt.Color(0, 0, 0));
        naissance.setColorDiaActual(new java.awt.Color(255, 102, 255));
        naissance.setColorForeground(new java.awt.Color(0, 0, 0));
        naissance.setColorSelForeground(new java.awt.Color(255, 255, 102));
        naissance.setColorTextDiaActual(new java.awt.Color(51, 51, 51));
        naissance.setPlaceholder("Cliquez pour selectionner une date");

        input_email.setBackground(new java.awt.Color(0, 102, 153));
        input_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_email.setForeground(new java.awt.Color(255, 255, 255));
        input_email.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_email.setOpaque(false);
        input_email.setPhColor(new java.awt.Color(255, 255, 255));
        input_email.setPlaceholder("Adresse Email du candidat");
        input_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_emailActionPerformed(evt);
            }
        });

        input_adresse.setBackground(new java.awt.Color(0, 102, 153));
        input_adresse.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_adresse.setForeground(new java.awt.Color(255, 255, 255));
        input_adresse.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_adresse.setOpaque(false);
        input_adresse.setPhColor(new java.awt.Color(255, 255, 255));
        input_adresse.setPlaceholder("Adresse du candidat");
        input_adresse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_adresseActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Adresse Mail");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Pays");

        input_tele.setBackground(new java.awt.Color(0, 102, 153));
        input_tele.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_tele.setForeground(new java.awt.Color(255, 255, 255));
        input_tele.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_tele.setOpaque(false);
        input_tele.setPhColor(new java.awt.Color(255, 255, 255));
        input_tele.setPlaceholder("Numero de téléphone  du candidat");
        input_tele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_teleActionPerformed(evt);
            }
        });

        sexe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));
        sexe.setColorArrow(new java.awt.Color(0, 102, 153));
        sexe.setColorBorde(new java.awt.Color(255, 255, 255));
        sexe.setColorFondo(new java.awt.Color(0, 102, 153));
        sexe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Téléphone");

        inpute_carte.setBackground(new java.awt.Color(0, 102, 153));
        inpute_carte.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        inpute_carte.setForeground(new java.awt.Color(255, 255, 255));
        inpute_carte.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        inpute_carte.setOpaque(false);
        inpute_carte.setPhColor(new java.awt.Color(255, 255, 255));
        inpute_carte.setPlaceholder("Numero de la Carte identité");
        inpute_carte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inpute_carteActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Carte Identité");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(vehicule_n)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(id_vehicule)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(152, 152, 152))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(inpute_carte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(67, 67, 67))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(input_nom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(input_prenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                        .addComponent(input_adresse, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(input_email, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(sexe, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel8))
                                .addGap(0, 50, Short.MAX_VALUE)
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel14))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel15)
                                                            .addComponent(input_tele, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(naissance, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                .addGap(99, 99, 99)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 8, Short.MAX_VALUE)))
                                        .addGap(67, 67, 67))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76))))))))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vehicule_n)
                        .addComponent(id_vehicule))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(input_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(input_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(input_adresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(input_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(input_tele, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(52, 52, 52)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(naissance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inpute_carte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void input_prenomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_prenomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_prenomActionPerformed

    private void input_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_nomActionPerformed

    private void rSMaterialButtonCircle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1MouseClicked
        // TODO add your handling code here:
        modifier_candidat();

    }//GEN-LAST:event_rSMaterialButtonCircle1MouseClicked

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        inserer_candidat();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void input_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_emailActionPerformed

    private void input_adresseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_adresseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_adresseActionPerformed

    private void input_teleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_teleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_teleActionPerformed

    private void inpute_carteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inpute_carteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inpute_carteActionPerformed

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
            java.util.logging.Logger.getLogger(ajouter_candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ajouter_candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ajouter_candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ajouter_candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ajouter_image;
    private javax.swing.JLabel id_vehicule;
    private app.bolivia.swing.JCTextField input_adresse;
    private app.bolivia.swing.JCTextField input_email;
    private app.bolivia.swing.JCTextField input_nom;
    private app.bolivia.swing.JCTextField input_prenom;
    private app.bolivia.swing.JCTextField input_tele;
    private app.bolivia.swing.JCTextField inpute_carte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private rojeru_san.componentes.RSDateChooser naissance;
    private rojerusan.RSComboMetro pays;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSComboMetro sexe;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables
}
