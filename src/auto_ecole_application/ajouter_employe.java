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
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author yazid
 */
public class ajouter_employe extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    String path = null;
    byte[] user_image = null;
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    private Employe instance;
    Acceuil acceuil = new Acceuil();

    /**
     * Creates new form ajouter_moniteur
     */
    public ajouter_employe(Employe instance) {
        this.instance = instance;
        initComponents();
        ajouter_img.addMouseListener(new java.awt.event.MouseAdapter() { // lorsque on clique sur le jlabel 
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
                    ajouter_img.setIcon(imageic);
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
            java.util.Date date1 = naissance.getDatoFecha();
            Long date_date1 = date1.getTime();
            java.sql.Date date_nais = new java.sql.Date(date_date1);
            if (input_nom.getText().isEmpty() || input_prenom.getText().isEmpty() || input_tele.getText().isEmpty() || input_email.getText().isEmpty()
                    || date_nais.equals("") || salaire.getText().isEmpty()) {
                valide = false;
                JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs ! ", null, JOptionPane.ERROR_MESSAGE);
            } else {
                if (!input_email.getText().matches("^.+@.+\\..+$")) {
                    JOptionPane.showMessageDialog(null, "L'email n'est pas valide !", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                } else if (input_tele.getText().length() != 9 || c1.est_chiffre(input_tele.getText()) == false) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "Numéro de téléphone est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                } else if (c1.calculer_age(date_nais) <= 24) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "L'âge du Moniteur doit être superieur à 24   ! ", null, JOptionPane.ERROR_MESSAGE);
                } else if (!c1.est_chiffre(salaire.getText())) {
                    valide = false;
                    JOptionPane.showMessageDialog(null, "Le Salaire est incorrecte ! ", null, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return valide;
    }

    public void inserer_moniteur() {
        if (valider_formulaire() == true) {
            String nom = input_nom.getText();
            String prenom = input_prenom.getText();
            String telephone = pays.getSelectedItem().toString() + input_tele.getText();
            String emails = input_email.getText();
            String sexes = sexe.getSelectedItem().toString();
            String permis = input_permis.getText();
            String salaires = salaire.getText();
            java.util.Date date1 = naissance.getDatoFecha();
            String type = types.getSelectedItem().toString();
            String username = user_name.getText();
            String password = mdps.getText();
            ArrayList<String> categories_liste = new ArrayList<>();
            if (ck1.isSelected()) {
                categories_liste.add(ck1.getText());
            }
            if (ck2.isSelected()) {
                categories_liste.add(ck2.getText());
            }
            if (ck3.isSelected()) {
                categories_liste.add(ck3.getText());
            }
            if (ck4.isSelected()) {
                categories_liste.add(ck4.getText());
            }
            if (ck5.isSelected()) {
                categories_liste.add(ck5.getText());
            }
            if (ck6.isSelected()) {
                categories_liste.add(ck6.getText());
            }
            if (ck7.isSelected()) {
                categories_liste.add(ck7.getText());
            }
            if (ck8.isSelected()) {
                categories_liste.add(ck8.getText());
            }
            if (ck9.isSelected()) {
                categories_liste.add(ck9.getText());
            }
            if (ck10.isSelected()) {
                categories_liste.add(ck10.getText());
            }
            if (ck11.isSelected()) {
                categories_liste.add(ck11.getText());
            }
            String[] categories_liste_tableau = categories_liste.toArray(new String[0]);
            Long date_date1 = date1.getTime();
            java.sql.Date date_nais = new java.sql.Date(date_date1);
            try {
                con = c1.connection_db();
                String types_sel = types.getSelectedItem().toString();
                switch (types_sel) {
                    case "Moniteur":
                        if (categories_liste_tableau.length == 0 || input_permis.getText().isEmpty() || user_name.getText().isEmpty() || mdps.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs", null, JOptionPane.ERROR_MESSAGE);
                        } else if (input_permis.getText().length() != 9 || c1.est_chiffre(input_permis.getText()) == false) {
                            JOptionPane.showMessageDialog(null, "Numéro du permis est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                        } else {
                            String sql = "insert into employe (nom,prenom,date_naissance,telephone,email,sexe,Numero_permis,salaire_mensuel,photo,user_name,mot_passe,categorie,type) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            PreparedStatement st = con.prepareStatement(sql);
                            st.setString(1, nom);
                            st.setString(2, prenom);
                            st.setDate(3, date_nais);
                            st.setString(4, telephone);
                            st.setString(5, emails);
                            st.setString(6, sexes);
                            st.setString(7, permis);
                            st.setString(8, salaires);
                            st.setBytes(9, user_image);
                            st.setString(10, username);
                            st.setString(11, password);
                            st.setString(12, String.join(",", categories_liste_tableau));
                            st.setString(13, type);
                            st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Moniteur ajouté avec succès attendez l'envoi de l'email pour fermer le formulaire", null, JOptionPane.INFORMATION_MESSAGE);
                            if (acceuil.EmailSender(emails, "Confirmation d'inscription", "Bonjour,\n\n Votre Inscription à l'application l'auto école à été bien effectué Voici les informations d'accès au système.\n\nNom d'utilisateur : " + username + "\n\nLe mot de passe : " + password + "\n\n\nMerci !\n\nCoridalement.") == true) {
                                JOptionPane.showMessageDialog(null, "Email envoyé avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                            }
                            instance.model.setRowCount(0);
                            instance.selectionner_employe();
                            instance.statistique();
                            this.dispose();
                        }
                        break;
                    case "Secrétaire":
                        if (user_name.getText().isEmpty() || mdps.getText().isEmpty()) {;
                            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs", null, JOptionPane.ERROR_MESSAGE);
                        } else {
                            String sql2 = "insert into employe (nom,prenom,date_naissance,telephone,email,sexe,salaire_mensuel,photo,user_name,mot_passe,type) values (?,?,?,?,?,?,?,?,?,?,?)";
                            PreparedStatement st2 = con.prepareStatement(sql2);
                            st2.setString(1, nom);
                            st2.setString(2, prenom);
                            st2.setDate(3, date_nais);
                            st2.setString(4, telephone);
                            st2.setString(5, emails);
                            st2.setString(6, sexes);
                            st2.setString(7, salaires);
                            st2.setBytes(8, user_image);
                            st2.setString(9, username);
                            st2.setString(10, password);
                            st2.setString(11, type);
                            st2.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Secrétaire ajouté avec succès attendez l'envoi de l'email pour fermer le formulaire", null, JOptionPane.INFORMATION_MESSAGE);
                            if (acceuil.EmailSender(emails, "Confirmation d'inscription", "Bonjour,\n\n Votre Inscription à l'application l'auto école à été bien effectué Voici les informations d'accès au système.\n\nNom d'utilisateur : " + username + "\n\nLe mot de passe : " + password + "\n\n\nMerci !\n\nCoridalement.") == true) {
                                JOptionPane.showMessageDialog(null, "Email envoyé avec succès", null, JOptionPane.INFORMATION_MESSAGE);

                            }
                            instance.model.setRowCount(0);
                            instance.selectionner_employe();
                            this.dispose();
                        }
                        break;
                    case "Autre":
                        String sql3 = "insert into employe (nom,prenom,date_naissance,telephone,email,sexe,salaire_mensuel,photo,type) values (?,?,?,?,?,?,?,?,?)";
                        PreparedStatement st3 = con.prepareStatement(sql3);
                        st3.setString(1, nom);
                        st3.setString(2, prenom);
                        st3.setDate(3, date_nais);
                        st3.setString(4, telephone);
                        st3.setString(5, emails);
                        st3.setString(6, sexes);
                        st3.setString(7, salaires);
                        st3.setBytes(8, user_image);
                        st3.setString(9, type);
                        st3.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Employe ajouté avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                        instance.model.setRowCount(0);
                        instance.selectionner_employe();
                        this.dispose();
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void affichage(String ids) {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from employe where id_employe=?");
            st.setString(1, ids);
            rs = st.executeQuery();
            while (rs.next()) {
                Blob blob1 = rs.getBlob("photo");
                byte[] imagebyte = blob1.getBytes(1, (int) blob1.length());
                ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
                ajouter_img.setIcon(imag);
                input_nom.setText(rs.getString("nom"));
                input_prenom.setText(rs.getString("prenom"));
                sexe.setSelectedItem(rs.getString("sexe").trim());
                types.setSelectedItem(rs.getString("type").trim());
                input_email.setText(rs.getString("email"));
                pays.setSelectedItem(rs.getString("telephone").substring(1, 3).trim());
                input_tele.setText(rs.getString("telephone").substring(3, 12));
                naissance.setDatoFecha(rs.getDate("date_naissance"));
                salaire.setText(rs.getString("salaire_mensuel"));
                vehicule_n.setText("Employé N°: ");
                id_vehicule.setText(ids);
                switch (rs.getString("type").trim()) {
                    case "Moniteur":
                        input_permis.setText(rs.getString("Numero_permis"));
                        String cat = rs.getString("categorie");
                        user_name.setText(rs.getString("user_name"));
                        mdps.setText(rs.getString("mot_passe"));
                        String[] categories = cat.split(",");
                        for (String categorie : categories) {
                            System.out.println(categorie);
                            switch (categorie) {
                                case "A":
                                    ck1.setSelected(true);
                                    break;
                                case "A1":
                                    ck2.setSelected(true);
                                    break;
                                case "B":
                                    ck3.setSelected(true);
                                    break;
                                case "C":
                                    ck4.setSelected(true);
                                    break;
                                case "C1":
                                    ck5.setSelected(true);
                                    break;
                                case "D":
                                    ck6.setSelected(true);
                                    break;
                                case "BE":
                                    ck7.setSelected(true);
                                    break;
                                case "CE":
                                    ck8.setSelected(true);
                                    break;
                                case "C1E":
                                    ck9.setSelected(true);
                                    break;
                                case "DE":
                                    ck10.setSelected(true);
                                    break;
                                case "F":
                                    ck11.setSelected(true);
                                    break;
                            }
                        }
                        break;
                    case "Secrétaire":
                        user_name.setText(rs.getString("user_name"));
                        mdps.setText(rs.getString("mot_passe"));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifier_Moniteur() {
        try {
            if (valider_formulaire() == true) {
                String nom = input_nom.getText();
                String prenom = input_prenom.getText();
                String telephone = pays.getSelectedItem().toString() + input_tele.getText();
                String emails = input_email.getText();
                String sexes = sexe.getSelectedItem().toString();
                String salaires = salaire.getText();
                java.util.Date date1 = naissance.getDatoFecha();
                String type = types.getSelectedItem().toString();
                String permis = input_permis.getText();
                String username = user_name.getText();
                String password = mdps.getText();
                ArrayList<String> categories_liste = new ArrayList<>();
                if (ck1.isSelected()) {
                    categories_liste.add(ck1.getText());
                }
                if (ck2.isSelected()) {
                    categories_liste.add(ck2.getText());
                }
                if (ck3.isSelected()) {
                    categories_liste.add(ck3.getText());
                }
                if (ck4.isSelected()) {
                    categories_liste.add(ck4.getText());
                }
                if (ck5.isSelected()) {
                    categories_liste.add(ck5.getText());
                }
                if (ck6.isSelected()) {
                    categories_liste.add(ck6.getText());
                }
                if (ck7.isSelected()) {
                    categories_liste.add(ck7.getText());
                }
                if (ck8.isSelected()) {
                    categories_liste.add(ck8.getText());
                }
                if (ck9.isSelected()) {
                    categories_liste.add(ck9.getText());
                }
                if (ck10.isSelected()) {
                    categories_liste.add(ck10.getText());
                }
                if (ck11.isSelected()) {
                    categories_liste.add(ck11.getText());
                }
                String[] categories_liste_tableau = categories_liste.toArray(new String[0]);
                Long date_date1 = date1.getTime();
                java.sql.Date date_nais = new java.sql.Date(date_date1);
                switch (type) {
                    case "Moniteur":
                        if (categories_liste_tableau.length == 0 || permis.isEmpty() || username.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs", null, JOptionPane.ERROR_MESSAGE);

                        } else if (permis.length() != 9 || c1.est_chiffre(permis) == false) {
                            JOptionPane.showMessageDialog(null, "Numéro du permis est incorrecte  ! ", null, JOptionPane.ERROR_MESSAGE);
                        } else {
                            String update_moniteur = "update employe set nom=?,prenom=?,date_naissance=?,telephone=?,email=?,sexe=?,Numero_permis=?,salaire_mensuel=?,user_name=?, mot_passe=?,categorie=? where id_employe=?";
                            PreparedStatement st = con.prepareStatement(update_moniteur);
                            st.setString(1, nom);
                            st.setString(2, prenom);
                            st.setDate(3, date_nais);
                            st.setString(4, telephone);
                            st.setString(5, emails);
                            st.setString(6, sexes);
                            st.setString(7, permis);
                            st.setString(8, salaires);
                            st.setString(9, username);
                            st.setString(10, password);
                            st.setString(11, String.join(",", categories_liste_tableau));
                            st.setString(12, id_vehicule.getText());
                            st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Moniteur modifié avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                            instance.model.setRowCount(0);
                            instance.selectionner_employe();
                            this.dispose();
                        }
                        break;
                    case "Secrétaire":
                        if (username.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs", null, JOptionPane.ERROR_MESSAGE);
                        } else {
                            String update_secretaire = "update employe set nom=?,prenom=?,date_naissance=?,telephone=?,email=?,sexe=?,salaire_mensuel=?,user_name=?, mot_passe=? where id_employe=?";
                            PreparedStatement st = con.prepareStatement(update_secretaire);
                            st.setString(1, nom);
                            st.setString(2, prenom);
                            st.setDate(3, date_nais);
                            st.setString(4, telephone);
                            st.setString(5, emails);
                            st.setString(6, sexes);
                            st.setString(7, salaires);
                            st.setString(8, username);
                            st.setString(9, password);
                            st.setString(10, id_vehicule.getText());
                            st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Secrétaire modifié avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                            instance.model.setRowCount(0);
                            instance.selectionner_employe();
                            this.dispose();
                        }
                        break;
                    case "Autre":
                        String update_autre = "update employe set nom=?,prenom=?,date_naissance=?,telephone=?,email=?,sexe=?,salaire_mensuel=? where id_employe=?";
                        PreparedStatement st = con.prepareStatement(update_autre);
                        st.setString(1, nom);
                        st.setString(2, prenom);
                        st.setDate(3, date_nais);
                        st.setString(4, telephone);
                        st.setString(5, emails);
                        st.setString(6, sexes);
                        st.setString(7, salaires);
                        st.setString(8, id_vehicule.getText());
                        st.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Employé modifié avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                        instance.model.setRowCount(0);
                        instance.selectionner_employe();
                        this.dispose();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        input_nom = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        pays = new rojerusan.RSComboMetro();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ajouter_img = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        input_email = new app.bolivia.swing.JCTextField();
        input_permis = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        input_tele = new app.bolivia.swing.JCTextField();
        sexe = new rojerusan.RSComboMetro();
        jLabel15 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        salaire = new app.bolivia.swing.JCTextField();
        input_prenom = new app.bolivia.swing.JCTextField();
        naissance = new rojeru_san.componentes.RSDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        user_name = new app.bolivia.swing.JCTextField();
        mdps = new javax.swing.JPasswordField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        ck1 = new javax.swing.JCheckBox();
        ck2 = new javax.swing.JCheckBox();
        ck3 = new javax.swing.JCheckBox();
        ck4 = new javax.swing.JCheckBox();
        ck5 = new javax.swing.JCheckBox();
        ck6 = new javax.swing.JCheckBox();
        ck7 = new javax.swing.JCheckBox();
        ck8 = new javax.swing.JCheckBox();
        ck9 = new javax.swing.JCheckBox();
        ck10 = new javax.swing.JCheckBox();
        ck11 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        types = new rojerusan.RSComboMetro();

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
        input_nom.setPlaceholder("Saisir le nom du moniteur");
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
            .addComponent(ajouter_img, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ajouter_img, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Choisir une Photo");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Permis");

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

        input_email.setBackground(new java.awt.Color(0, 102, 153));
        input_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_email.setForeground(new java.awt.Color(255, 255, 255));
        input_email.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_email.setOpaque(false);
        input_email.setPhColor(new java.awt.Color(255, 255, 255));
        input_email.setPlaceholder("Adresse Email du moniteur");
        input_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_emailActionPerformed(evt);
            }
        });

        input_permis.setBackground(new java.awt.Color(0, 102, 153));
        input_permis.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_permis.setForeground(new java.awt.Color(255, 255, 255));
        input_permis.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_permis.setOpaque(false);
        input_permis.setPhColor(new java.awt.Color(255, 255, 255));
        input_permis.setPlaceholder("Numero du permis");
        input_permis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_permisActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Pays");

        input_tele.setBackground(new java.awt.Color(0, 102, 153));
        input_tele.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_tele.setForeground(new java.awt.Color(255, 255, 255));
        input_tele.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_tele.setOpaque(false);
        input_tele.setPhColor(new java.awt.Color(255, 255, 255));
        input_tele.setPlaceholder("558.....");
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

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Salaire Mensuel");

        salaire.setBackground(new java.awt.Color(0, 102, 153));
        salaire.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        salaire.setForeground(new java.awt.Color(255, 255, 255));
        salaire.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        salaire.setOpaque(false);
        salaire.setPhColor(new java.awt.Color(255, 255, 255));
        salaire.setPlaceholder("Salaire Par mois");
        salaire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salaireActionPerformed(evt);
            }
        });

        input_prenom.setBackground(new java.awt.Color(0, 102, 153));
        input_prenom.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_prenom.setForeground(new java.awt.Color(255, 255, 255));
        input_prenom.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_prenom.setOpaque(false);
        input_prenom.setPhColor(new java.awt.Color(255, 255, 255));
        input_prenom.setPlaceholder("Saisir le prénom du moniteur");
        input_prenom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_prenomActionPerformed(evt);
            }
        });

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

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Adresse Email");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Nom d'utilisateur");
        jLabel17.setToolTipText("");

        user_name.setBackground(new java.awt.Color(0, 102, 153));
        user_name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        user_name.setForeground(new java.awt.Color(255, 255, 255));
        user_name.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        user_name.setOpaque(false);
        user_name.setPhColor(new java.awt.Color(255, 255, 255));
        user_name.setPlaceholder("Saisir un nom d'utisateur");
        user_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_nameActionPerformed(evt);
            }
        });

        mdps.setBackground(new java.awt.Color(0, 102, 153));
        mdps.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        mdps.setForeground(new java.awt.Color(255, 255, 255));
        mdps.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Mot de passe");
        jLabel18.setToolTipText("");

        jLabel20.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Catégorie des permis");

        ck1.setBackground(new java.awt.Color(0, 102, 153));
        ck1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck1.setForeground(new java.awt.Color(255, 255, 255));
        ck1.setText("A");
        ck1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck1ActionPerformed(evt);
            }
        });

        ck2.setBackground(new java.awt.Color(0, 102, 153));
        ck2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck2.setForeground(new java.awt.Color(255, 255, 255));
        ck2.setText("A1");
        ck2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck2ActionPerformed(evt);
            }
        });

        ck3.setBackground(new java.awt.Color(0, 102, 153));
        ck3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck3.setForeground(new java.awt.Color(255, 255, 255));
        ck3.setText("B");
        ck3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck3ActionPerformed(evt);
            }
        });

        ck4.setBackground(new java.awt.Color(0, 102, 153));
        ck4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck4.setForeground(new java.awt.Color(255, 255, 255));
        ck4.setText("C");
        ck4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck4ActionPerformed(evt);
            }
        });

        ck5.setBackground(new java.awt.Color(0, 102, 153));
        ck5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck5.setForeground(new java.awt.Color(255, 255, 255));
        ck5.setText("C1");
        ck5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck5ActionPerformed(evt);
            }
        });

        ck6.setBackground(new java.awt.Color(0, 102, 153));
        ck6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck6.setForeground(new java.awt.Color(255, 255, 255));
        ck6.setText("D");
        ck6.setToolTipText("");
        ck6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck6ActionPerformed(evt);
            }
        });

        ck7.setBackground(new java.awt.Color(0, 102, 153));
        ck7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck7.setForeground(new java.awt.Color(255, 255, 255));
        ck7.setText("BE");
        ck7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck7ActionPerformed(evt);
            }
        });

        ck8.setBackground(new java.awt.Color(0, 102, 153));
        ck8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck8.setForeground(new java.awt.Color(255, 255, 255));
        ck8.setText("CE");
        ck8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck8ActionPerformed(evt);
            }
        });

        ck9.setBackground(new java.awt.Color(0, 102, 153));
        ck9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck9.setForeground(new java.awt.Color(255, 255, 255));
        ck9.setText("C1E");
        ck9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck9ActionPerformed(evt);
            }
        });

        ck10.setBackground(new java.awt.Color(0, 102, 153));
        ck10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck10.setForeground(new java.awt.Color(255, 255, 255));
        ck10.setText("DE");
        ck10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck10ActionPerformed(evt);
            }
        });

        ck11.setBackground(new java.awt.Color(0, 102, 153));
        ck11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ck11.setForeground(new java.awt.Color(255, 255, 255));
        ck11.setText("F");
        ck11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck11ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Type");

        types.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Moniteur", "Secrétaire", "Autre" }));
        types.setColorArrow(new java.awt.Color(0, 102, 153));
        types.setColorBorde(new java.awt.Color(255, 255, 255));
        types.setColorFondo(new java.awt.Color(0, 102, 153));
        types.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        types.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                typesItemStateChanged(evt);
            }
        });

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
                        .addGap(95, 95, 95))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel17)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(naissance, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(input_nom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(input_permis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(input_email, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(87, 87, 87))
                                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(ck4)
                                                .addComponent(user_name, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addComponent(ck1)
                                        .addGap(36, 36, 36)
                                        .addComponent(ck2)
                                        .addGap(51, 51, 51)
                                        .addComponent(ck3)))
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel7)
                                                    .addComponent(salaire, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6)
                                                    .addComponent(sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ck10)
                                                .addGap(48, 48, 48)
                                                .addComponent(ck11)))
                                        .addGap(39, 39, 39))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addComponent(ck6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ck7)
                                                .addGap(53, 53, 53)
                                                .addComponent(ck8)
                                                .addGap(49, 49, 49)
                                                .addComponent(ck9)
                                                .addGap(204, 204, 204))
                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(input_prenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(jLabel14)
                                                                    .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(jLabel15)
                                                                    .addComponent(input_tele, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))))
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(jLabel3)
                                                                    .addComponent(jLabel8))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(types, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGap(18, 18, 18)))
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(23, 23, 23))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(mdps, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(149, 149, 149))))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(377, 377, 377)
                                .addComponent(jLabel20)
                                .addContainerGap())
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ck5)
                                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(vehicule_n)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_vehicule)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(22, 22, 22))))
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
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel2)
                                                    .addComponent(jLabel3))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(input_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(input_prenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(42, 42, 42)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jLabel8))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(input_permis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(types, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                                                .addGap(44, 44, 44)
                                                                .addComponent(jLabel16))
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel15)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(input_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(input_tele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                                                        .addGap(43, 43, 43)
                                                        .addComponent(jLabel14)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pays, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(naissance, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sexe, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36))
                                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(salaire, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(39, 39, 39)))
                                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(user_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(mdps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addComponent(jLabel20)
                        .addGap(43, 43, 43)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ck1)
                            .addComponent(ck2)
                            .addComponent(ck3)
                            .addComponent(ck4)
                            .addComponent(ck5)
                            .addComponent(ck6)
                            .addComponent(ck7)
                            .addComponent(ck8)
                            .addComponent(ck9)
                            .addComponent(ck10)
                            .addComponent(ck11))
                        .addContainerGap(192, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1033, Short.MAX_VALUE)
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

    private void input_nomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_nomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_nomActionPerformed

    private void rSMaterialButtonCircle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1MouseClicked
        // TODO add your handling code here:
        modifier_Moniteur();

    }//GEN-LAST:event_rSMaterialButtonCircle1MouseClicked

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        inserer_moniteur();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void input_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_emailActionPerformed

    private void input_teleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_teleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_teleActionPerformed

    private void salaireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salaireActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salaireActionPerformed

    private void input_permisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_permisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_permisActionPerformed

    private void input_prenomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_prenomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_prenomActionPerformed

    private void user_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_nameActionPerformed

    private void ck2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck2ActionPerformed

    private void ck3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck3ActionPerformed

    private void ck4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck4ActionPerformed

    private void ck5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck5ActionPerformed

    private void ck7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck7ActionPerformed

    private void ck8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck8ActionPerformed

    private void ck9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck9ActionPerformed

    private void ck10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck10ActionPerformed

    private void ck11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck11ActionPerformed

    private void ck1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck1ActionPerformed

    private void ck6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck6ActionPerformed

    private void typesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_typesItemStateChanged
        // TODO add your handling code here:
        String types_sel = types.getSelectedItem().toString();
        switch (types_sel) {
            case "Secrétaire":
                ck1.setEnabled(false);
                ck2.setEnabled(false);
                ck3.setEnabled(false);
                ck4.setEnabled(false);
                ck5.setEnabled(false);
                ck6.setEnabled(false);
                ck7.setEnabled(false);
                ck8.setEnabled(false);
                ck9.setEnabled(false);
                ck10.setEnabled(false);
                ck11.setEnabled(false);
                input_permis.setEnabled(false);
                user_name.setEnabled(true);
                mdps.setEnabled(true);
                break;
            case "Moniteur":
                ck1.setEnabled(true);
                ck2.setEnabled(true);
                ck3.setEnabled(true);
                ck4.setEnabled(true);
                ck5.setEnabled(true);
                ck6.setEnabled(true);
                ck7.setEnabled(true);
                ck8.setEnabled(true);
                ck9.setEnabled(true);
                ck10.setEnabled(true);
                ck11.setEnabled(true);
                input_permis.setEnabled(true);
                user_name.setEnabled(true);
                mdps.setEnabled(true);
                break;
            case "Autre":
                ck1.setEnabled(false);
                ck2.setEnabled(false);
                ck3.setEnabled(false);
                ck4.setEnabled(false);
                ck5.setEnabled(false);
                ck6.setEnabled(false);
                ck7.setEnabled(false);
                ck8.setEnabled(false);
                ck9.setEnabled(false);
                ck10.setEnabled(false);
                ck11.setEnabled(false);
                input_permis.setEnabled(false);
                user_name.setEnabled(false);
                mdps.setEnabled(false);
                break;
        }
    }//GEN-LAST:event_typesItemStateChanged

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
            java.util.logging.Logger.getLogger(ajouter_employe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ajouter_employe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ajouter_employe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ajouter_employe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ajouter_img;
    private javax.swing.JCheckBox ck1;
    private javax.swing.JCheckBox ck10;
    private javax.swing.JCheckBox ck11;
    private javax.swing.JCheckBox ck2;
    private javax.swing.JCheckBox ck3;
    private javax.swing.JCheckBox ck4;
    private javax.swing.JCheckBox ck5;
    private javax.swing.JCheckBox ck6;
    private javax.swing.JCheckBox ck7;
    private javax.swing.JCheckBox ck8;
    private javax.swing.JCheckBox ck9;
    private javax.swing.JLabel id_vehicule;
    private app.bolivia.swing.JCTextField input_email;
    private app.bolivia.swing.JCTextField input_nom;
    private app.bolivia.swing.JCTextField input_permis;
    private app.bolivia.swing.JCTextField input_prenom;
    private app.bolivia.swing.JCTextField input_tele;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JPasswordField mdps;
    private rojeru_san.componentes.RSDateChooser naissance;
    private rojerusan.RSComboMetro pays;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private app.bolivia.swing.JCTextField salaire;
    private rojerusan.RSComboMetro sexe;
    private rojerusan.RSComboMetro types;
    private app.bolivia.swing.JCTextField user_name;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables
}
