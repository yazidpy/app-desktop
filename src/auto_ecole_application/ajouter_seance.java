/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author yazid
 */
public class ajouter_seance extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Acceuil acceuil = new Acceuil();
    Connection con;
    Statement st;
    ResultSet rs;
    private cours_app instance;

    /**
     * Creates new form ajouter_seance
     */
    public ajouter_seance(cours_app instance) {
        this.instance = instance;
        initComponents();
        LocalDate date_de_jour = LocalDate.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_actuell_maintenant = date_de_jour.format(formater);
        selectionner_candidat();
        selectionner_groupe();
        selectionner_vehicule();
        selectionner_moniteurs();
    }

    public void selectionner_candidat() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select c.nom,c.prenom from candidat c , candidat_inscrie_formation cf where c.id_candidat=cf.candidat_id_candidat ");
            rs = st.executeQuery();
            while (rs.next()) {
                Object nom = rs.getString("nom");
                Object prenom = rs.getString("prenom");
                candidat_choix.addItem(nom + "," + prenom);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectionner_moniteurs() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select nom,prenom from employe where type=?");
            st.setString(1, "Moniteur");
            rs = st.executeQuery();
            while (rs.next()) {
                Object nom = rs.getString("nom");
                Object prenom = rs.getString("prenom");
                choix_moniteur.addItem(nom + "," + prenom);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectionner_vehicule() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select matricule from vehicule");
            rs = st.executeQuery();
            while (rs.next()) {
                Object mat = rs.getString(1);
                vehicule_choix.addItem(mat);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectionner_groupe() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select nom from groupe");
            rs = st.executeQuery();
            while (rs.next()) {
                Object nom = rs.getString(1);
                groupe_choix.addItem(nom);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void type_seance() {
        String type_seance = seance_choix.getSelectedItem().toString();
        switch (type_seance) {
            case "Aucun":
                JOptionPane.showMessageDialog(null, "Vous devez choisir un type de séance !", null, JOptionPane.ERROR_MESSAGE);
                break;
            case "Code":
                vehicule_choix.disable();
                candidat_choix.disable();
                vehicule_choix.setSelectedItem("Aucun");
                candidat_choix.setSelectedItem("Aucun");
                groupe_choix.enable();
                choix_horaire.removeAllItems();
                choix_horaire.addItem("Aucun");
                choix_horaire.addItem("08:00 -- 08:45");
                choix_horaire.addItem("08:45 -- 09:30");
                choix_horaire.addItem("09:45 -- 10:30");
                choix_horaire.addItem("10:30 -- 11:15");
                choix_horaire.addItem("11:15 -- 12:00");
                choix_horaire.addItem("13:00 -- 13:45");
                choix_horaire.addItem("13:45 -- 14:30");
                choix_horaire.addItem("14:30 -- 15:15");
                choix_horaire.addItem("15:15 -- 16:00");
                choix_horaire.addItem("16:00 -- 16:45");

                break;
            case "Créneau":
                vehicule_choix.enable();
                candidat_choix.enable();
                groupe_choix.disable();
                groupe_choix.setSelectedItem("Aucun");
                choix_horaire.removeAllItems();
                choix_horaire.addItem("Aucun");
                choix_horaire.addItem("08:00 -- 10:00");
                choix_horaire.addItem("10:00 -- 12:00");
                choix_horaire.addItem("13:00 -- 15:00");
                choix_horaire.addItem("15:00 -- 17:00");
                break;
            case "Conduite":
                vehicule_choix.enable();
                candidat_choix.enable();
                groupe_choix.disable();
                groupe_choix.setSelectedItem("Aucun");
                choix_horaire.removeAllItems();
                choix_horaire.addItem("Aucun");
                choix_horaire.addItem("08:00 -- 10:00");
                choix_horaire.addItem("10:00 -- 12:00");
                choix_horaire.addItem("13:00 -- 15:00");
                choix_horaire.addItem("15:00 -- 17:00");
                break;
        }
    }
    public boolean valider_formulaire() {
        java.util.Date date_cour = date_cours.getDatoFecha();
        Long date_date = date_cour.getTime();
        java.sql.Date date_cours_sql = new java.sql.Date(date_date);
        boolean valide = true;
        if (choix_horaire.getSelectedItem().toString().equals("Aucun") || lieu.getText().isEmpty() || seance_choix.getSelectedItem().toString() == "Aucun" || choix_moniteur.getSelectedItem().toString().equals("Aucun") || date_cours.getFormatoFecha().toString().equals("")) {
            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        } else {
            if (comparer_date(date_cours_sql) <= 0) {
                JOptionPane.showMessageDialog(null, "La date du cours doit être au mois supérieur d'une journée de la date actuelle !", null, JOptionPane.ERROR_MESSAGE);
                valide = false;
            }
        }
        return valide;
    }

    public boolean vehicule_disponible(int ids, String horaire, Date date) {
        boolean disponible = true;
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from seance where vehicule_id_vehicule=? and heure=? and date=?");
            st.setInt(1, ids);
            st.setString(2, horaire);
            st.setDate(3, date);
            rs = st.executeQuery();
            if (rs.next()) {
                disponible = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return disponible;
    }

    public int comparer_date(java.sql.Date Date_en_parametre) {
        LocalDate date_saisi = Date_en_parametre.toLocalDate();
        LocalDate date_actuel = LocalDate.now();
        Period difference = Period.between(date_actuel, date_saisi);
        int diff = difference.getDays();
        return diff;
    }

    public boolean moniteur_disponible(int ids, String heure_debuts, Date dates) {
        boolean disponible = true;
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from seance where moniteur_employe_id=?");
            st.setInt(1, ids);
            rs = st.executeQuery();
            while (rs.next()) {
                String heure_debut_bdd = rs.getString("heure");
                Date date_s = rs.getDate("date");
                if (!date_s.equals(dates)) {
                    disponible = true;
                } else {
                    if (heure_debut_bdd.equals(heure_debuts)) {
                        JOptionPane.showMessageDialog(null, "Le moniteur n'est pas disponible à l'heure choisie !", null, JOptionPane.ERROR_MESSAGE);
                        disponible = false;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }

        return disponible;
    }

    public void inserer_seance() {
        try {
            if (valider_formulaire() == true) {
                java.util.Date date_cr = date_cours.getDatoFecha();
                Long date_date = date_cr.getTime();
                java.sql.Date date_cours_sql = new java.sql.Date(date_date);
                String s_lieu = lieu.getText();
                String heure = choix_horaire.getSelectedItem().toString();
                String moniteur = choix_moniteur.getSelectedItem().toString();
                String groupe = groupe_choix.getSelectedItem().toString();
                String candidat = candidat_choix.getSelectedItem().toString();
                String vehicule = vehicule_choix.getSelectedItem().toString();
                String nom_prenom_moniteur[] = moniteur.split(",");
                String nom_moniteur = nom_prenom_moniteur[0];
                String prenom_moniteur = nom_prenom_moniteur[1];
                con = c1.connection_db();
                String type_seance = seance_choix.getSelectedItem().toString();
                PreparedStatement moniteur_statement = con.prepareStatement("select id_employe from employe where nom=? and prenom=?");
                moniteur_statement.setString(1, nom_moniteur);
                moniteur_statement.setString(2, prenom_moniteur);
                rs = moniteur_statement.executeQuery();
                int id_moniteur = 0;
                int id_groupe = 0;
                int vehicule_id = 0;
                int id_candidat = 0;
                if (rs.next()) {
                    id_moniteur = rs.getInt("id_employe");
                }
                if (moniteur_disponible(id_moniteur, heure, date_cours_sql) == true) {
                    String message_moniteur = "";
                    String objet = "Convocation à une séance de cours à l'auto-école";
                    String emails_moniteur = "";
                    String message_candidat = "";
                    PreparedStatement statement_email_moniteur = con.prepareStatement("select email from employe where id_employe=?");
                    statement_email_moniteur.setInt(1, id_moniteur);
                    ResultSet rs4 = statement_email_moniteur.executeQuery();
                    if (rs4.next()) {
                        emails_moniteur = rs4.getString("email");
                        message_moniteur = "Cher/chère Moniteur  " + nom_moniteur + " " + prenom_moniteur + "\n"
                                + "J'espère que ce message vous trouvera en bonne santé et en forme. Je vous écris pour vous convier à une séance de " + type_seance + ".\n\n\n"
                                + "Voici les détails de la séance :\n"
                                + "\n"
                                + "Date :" + date_cours_sql + "\n\n"
                                + "Horaire   :" + heure + "\n\n"
                                + "Lieu : " + s_lieu + "\n\n"
                                + "Cordialement,\n\n"
                                + "Auto Ecole De Béjaia."
                                + "\n"
                                + "";
                    }
                    switch (type_seance) {
                        case "Code":
                            if (groupe.equals("Aucun")) {
                                JOptionPane.showMessageDialog(null, "Vous devez choisir un groupe !", null, JOptionPane.ERROR_MESSAGE);
                            } else {
                                PreparedStatement groupe_id = con.prepareStatement("select id from groupe where nom=?");
                                groupe_id.setString(1, groupe);
                                ResultSet rs1 = groupe_id.executeQuery();
                                if (rs1.next()) {
                                    id_groupe = rs1.getInt("id");
                                }
                                if (existe_seance_groupe(id_groupe, date_cours_sql) == false) {
                                    PreparedStatement les_emails_des_cand = con.prepareStatement("select nom,prenom,email from candidat where groupe=?");
                                    les_emails_des_cand.setInt(1, id_groupe);
                                    ResultSet rs7 = les_emails_des_cand.executeQuery();
                                    ArrayList<String> les_emails_des_candidat = new ArrayList<>();
                                    ArrayList<String> les_noms_des_candidat = new ArrayList<>();
                                    ArrayList<String> les_prenoms_des_candidat = new ArrayList<>();
                                    while (rs7.next()) {
                                        les_noms_des_candidat.add(rs7.getString("nom"));
                                        les_prenoms_des_candidat.add(rs7.getString("prenom"));
                                        les_emails_des_candidat.add(rs7.getString("email"));
                                    }
                                    String inserer1 = "insert into seance (type,date,heure,lieu,moniteur_employe_id,groupe_id) values (?,?,?,?,?,?)";
                                    PreparedStatement insert1 = con.prepareStatement(inserer1);
                                    insert1.setString(1, type_seance);
                                    insert1.setDate(2, date_cours_sql);
                                    insert1.setString(3, heure);
                                    insert1.setString(4, s_lieu);
                                    insert1.setInt(5, id_moniteur);
                                    insert1.setInt(6, id_groupe);
                                    insert1.executeUpdate();
                                    boolean reussi = true;
                                    JOptionPane.showMessageDialog(null, "Séance de " + type_seance + " ajouté avec succès attendez la confirmation des emails pour quitter !", null, JOptionPane.INFORMATION_MESSAGE);
                                    if (acceuil.EmailSender(emails_moniteur, objet, message_moniteur)) {
                                        String nom_can = "";
                                        String prenom_can = "";
                                        String email_can = "";
                                        for (int i = 0; i < les_prenoms_des_candidat.size(); i++) {
                                            email_can = les_emails_des_candidat.get(i);
                                            nom_can = les_noms_des_candidat.get(i);
                                            prenom_can = les_prenoms_des_candidat.get(i);
                                            message_candidat = "Cher/chère candidat(e) : " + nom_can + " " + prenom_can + "\n"
                                                    + "\n"
                                                    + "Nous avons le plaisir de vous informer que vous êtes convoqué(e) à une séance de cours de " + type_seance + " à l'auto-école. "
                                                    + "Cette séance est obligatoire et nécessaire pour votre réussite à l'examen pratique.\n"
                                                    + "\n"
                                                    + "Voici les détails de la séance :\n\n\n"
                                                    + "\n"
                                                    + "Date :" + date_cours_sql + ".\n\n"
                                                    + "Horaire   :" + heure + ".\n\n"
                                                    + "\n"
                                                    + "Lieu : " + s_lieu + " .\n\n"
                                                    + "Cordialement,\n\n"
                                                    + "Auto école de Bejaia"
                                                    + "";
                                            if (acceuil.EmailSender(email_can, objet, message_candidat)) {
                                                System.out.println("Enovye pour " + i);
                                            } else {
                                                reussi = false;
                                            }
                                        }
                                    } else {
                                        reussi = false;
                                    }
                                    if (reussi == false) {
                                        JOptionPane.showMessageDialog(null, "Une erreur de l'envoi des emails !", null, JOptionPane.ERROR_MESSAGE);
                                    }
                                    else{
                                        this.dispose();
                                        instance.selectionner_seances();
                                    }
                                }
                            }
                            break;
                        case "Conduite":
                        case "Créneau":
                            if (vehicule.equals("Aucun") || candidat == "Aucun") {
                                JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
                            } else {
                                String email = "";
                                String nom_prenom_candidat[] = candidat.split(",");
                                String nom_candidat = nom_prenom_candidat[0];
                                String prenom_candidat = nom_prenom_candidat[1];
                                PreparedStatement vehicule_statement = con.prepareStatement("select id_vehicule from vehicule where matricule=?");
                                vehicule_statement.setString(1, vehicule);
                                ResultSet rs2 = vehicule_statement.executeQuery();
                                if (rs2.next()) {
                                    vehicule_id = rs2.getInt("id_vehicule");
                                }
                                if (vehicule_disponible(vehicule_id, heure, date_cours_sql) == false) {
                                    JOptionPane.showMessageDialog(null, "Ce vehicule n'est pas disponible pour le créneau choisi !", null, JOptionPane.ERROR_MESSAGE);
                                }else{
                                PreparedStatement candidat_statement = con.prepareStatement("select id_candidat,email from candidat where nom=? and prenom=?");
                                candidat_statement.setString(1, nom_candidat);
                                candidat_statement.setString(2, prenom_candidat);
                                ResultSet rs3 = candidat_statement.executeQuery();
                                if (rs3.next()) {
                                    id_candidat = rs3.getInt("id_candidat");
                                    email = rs3.getString("email");
                                }
                                if (existe_seance_candidat(id_candidat, date_cours_sql) == false) {
                                    message_candidat = "Cher/chère candidat(e) : " + nom_candidat + " " + prenom_candidat + "\n"
                                            + "\n"
                                            + "Nous avons le plaisir de vous informer que vous êtes convoqué(e) à une séance de cours de " + type_seance + " à l'auto-école. "
                                            + "Cette séance est obligatoire et nécessaire pour votre réussite à l'examen pratique.\n"
                                            + "\n"
                                            + "Voici les détails de la séance :\n\n\n"
                                            + "\n"
                                            + "Date :" + date_cours_sql + ".\n\n"
                                            + "Horaire   :" + heure + ".\n\n"
                                            + "\n"
                                            + "Lieu : " + s_lieu + " .\n\n"
                                            + "Cordialement,\n\n"
                                            + "Auto école de Bejaia"
                                            + "";
                                    String inserer2 = "insert into seance (type,date,heure,lieu,moniteur_employe_id,vehicule_id_vehicule,candidat_id_candidat) values (?,?,?,?,?,?,?)";
                                    PreparedStatement insert2 = con.prepareStatement(inserer2);
                                    insert2.setString(1, type_seance);
                                    insert2.setDate(2, date_cours_sql);
                                    insert2.setString(3, heure);
                                    insert2.setString(4, s_lieu);
                                    insert2.setInt(5, id_moniteur);
                                    insert2.setInt(6, vehicule_id);
                                    insert2.setInt(7, id_candidat);
                                    insert2.executeUpdate();
                                    JOptionPane.showMessageDialog(null, "Séance de " + type_seance + " ajouté avec succès attendez la confirmation des emails pour quitter !", null, JOptionPane.INFORMATION_MESSAGE);
                                    if (acceuil.EmailSender(emails_moniteur, objet, message_moniteur)) {
                                        if (acceuil.EmailSender(email, objet, message_candidat)) {
                                            this.dispose();
                                            instance.selectionner_seances();
                                        }
                                    }
                                }
                            }
                            }
                            break;
                    }
                }
            }
        } catch (NullPointerException nul) {
            JOptionPane.showMessageDialog(null, "Vous devez selectionner une date", null, JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getClass().getSimpleName(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean existe_seance_groupe(int groupe, Date date_seance) {
        boolean existe = false;
        try {
            con = c1.connection_db();
            PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM seance where groupe_id=? and date=?");
            pst.setInt(1, groupe);
            pst.setDate(2, date_seance);
            rs = pst.executeQuery();
            rs.next();
            int count1 = rs.getInt(1);
            if (count1 > 0) {
                JOptionPane.showMessageDialog(null, "Ce Groupe a déja une séance le :" + date_seance, null, JOptionPane.ERROR_MESSAGE);
                existe = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return existe;
    }

    public boolean existe_seance_candidat(int candidat, Date date_seance) {
        boolean existe = false;
        try {
            con = c1.connection_db();
            PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM seance where candidat_id_candidat=? and date=?");
            pst.setInt(1, candidat);
            pst.setDate(2, date_seance);
            rs = pst.executeQuery();
            rs.next();
            int count1 = rs.getInt(1);
            if (count1 > 0) {
                JOptionPane.showMessageDialog(null, "Ce candidat a déja une séance le :" + date_seance, null, JOptionPane.ERROR_MESSAGE);
                existe = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return existe;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timePicker1 = new com.raven.swing.TimePicker();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        groupe_choix = new rojerusan.RSComboMetro();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        seance_choix = new rojerusan.RSComboMetro();
        label_veh = new javax.swing.JLabel();
        vehicule_choix = new rojerusan.RSComboMetro();
        date_cours = new rojeru_san.componentes.RSDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lieu = new app.bolivia.swing.JCTextField();
        choix_moniteur = new rojerusan.RSComboMetro();
        jLabel17 = new javax.swing.JLabel();
        candidat_choix = new rojerusan.RSComboMetro();
        choix_horaire = new rojerusan.RSComboMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        kGradientPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel3.setkEndColor(new java.awt.Color(0, 102, 153));
        kGradientPanel3.setkGradientFocus(1000);
        kGradientPanel3.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

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

        groupe_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        groupe_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        groupe_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        groupe_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        groupe_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe_choix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                groupe_choixItemStateChanged(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Groupe");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Moniteur");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Type de séance");

        seance_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun", "Code", "Créneau", "Conduite" }));
        seance_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        seance_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        seance_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        seance_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        seance_choix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                seance_choixItemStateChanged(evt);
            }
        });

        label_veh.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_veh.setForeground(new java.awt.Color(255, 255, 255));
        label_veh.setText("Véhicule");

        vehicule_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        vehicule_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        vehicule_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        vehicule_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        vehicule_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        date_cours.setBackground(new java.awt.Color(0, 102, 153));
        date_cours.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 1, 2, 1, new java.awt.Color(255, 255, 255)));
        date_cours.setForeground(new java.awt.Color(255, 204, 204));
        date_cours.setColorBackground(new java.awt.Color(0, 102, 153));
        date_cours.setColorButtonHover(new java.awt.Color(0, 0, 0));
        date_cours.setColorDiaActual(new java.awt.Color(255, 102, 255));
        date_cours.setColorForeground(new java.awt.Color(0, 0, 0));
        date_cours.setColorSelForeground(new java.awt.Color(255, 255, 102));
        date_cours.setColorTextDiaActual(new java.awt.Color(51, 51, 51));
        date_cours.setPlaceholder("Cliquez pour selectionner une date");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Horaire");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Date du cours");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Lieu");

        lieu.setBackground(new java.awt.Color(0, 102, 153));
        lieu.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        lieu.setForeground(new java.awt.Color(255, 255, 255));
        lieu.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lieu.setOpaque(false);
        lieu.setPhColor(new java.awt.Color(255, 255, 255));
        lieu.setPlaceholder("Choisir un lieu");
        lieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lieuMouseClicked(evt);
            }
        });
        lieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lieuActionPerformed(evt);
            }
        });

        choix_moniteur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        choix_moniteur.setColorArrow(new java.awt.Color(0, 102, 153));
        choix_moniteur.setColorBorde(new java.awt.Color(255, 255, 255));
        choix_moniteur.setColorFondo(new java.awt.Color(0, 102, 153));
        choix_moniteur.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Candidat");

        candidat_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        candidat_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        candidat_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        candidat_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        candidat_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat_choix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                candidat_choixItemStateChanged(evt);
            }
        });

        choix_horaire.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        choix_horaire.setColorArrow(new java.awt.Color(0, 102, 153));
        choix_horaire.setColorBorde(new java.awt.Color(255, 255, 255));
        choix_horaire.setColorFondo(new java.awt.Color(0, 102, 153));
        choix_horaire.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        choix_horaire.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                choix_horaireItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(kGradientPanel3Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(vehicule_n)
                            .addGap(7, 7, 7)
                            .addComponent(id_vehicule)
                            .addGap(719, 719, 719)
                            .addComponent(jLabel3))
                        .addGroup(kGradientPanel3Layout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                    .addComponent(seance_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(47, 47, 47)
                                    .addComponent(choix_moniteur, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(234, 234, 234)
                                    .addComponent(jLabel12))))
                        .addGroup(kGradientPanel3Layout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel17)
                                    .addGap(296, 296, 296)
                                    .addComponent(jLabel15))
                                .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                    .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(groupe_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(47, 47, 47)
                                    .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(vehicule_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label_veh)))))
                        .addGroup(kGradientPanel3Layout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                    .addComponent(choix_horaire, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lieu, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel14)))
                        .addGroup(kGradientPanel3Layout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addComponent(candidat_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(47, 47, 47)
                            .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addComponent(date_cours, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vehicule_n)
                    .addComponent(id_vehicule)
                    .addComponent(jLabel3))
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(46, 46, 46)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choix_moniteur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seance_choix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_veh)
                    .addComponent(jLabel11))
                .addGap(28, 28, 28)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vehicule_choix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupe_choix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel15))
                .addGap(43, 43, 43)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(candidat_choix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(date_cours, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16))
                .addGap(36, 36, 36)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choix_horaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        inserer_seance();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void groupe_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_groupe_choixItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_groupe_choixItemStateChanged

    private void seance_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_seance_choixItemStateChanged
        // TODO add your handling code here:
        type_seance();

    }//GEN-LAST:event_seance_choixItemStateChanged

    private void lieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lieuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lieuMouseClicked

    private void lieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lieuActionPerformed

    private void candidat_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_candidat_choixItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_candidat_choixItemStateChanged

    private void choix_horaireItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_choix_horaireItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_choix_horaireItemStateChanged

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
            java.util.logging.Logger.getLogger(ajouter_seance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ajouter_seance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ajouter_seance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ajouter_seance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSComboMetro candidat_choix;
    private rojerusan.RSComboMetro choix_horaire;
    private rojerusan.RSComboMetro choix_moniteur;
    private rojeru_san.componentes.RSDateChooser date_cours;
    private rojerusan.RSComboMetro groupe_choix;
    private javax.swing.JLabel id_vehicule;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private keeptoo.KGradientPanel kGradientPanel3;
    private javax.swing.JLabel label_veh;
    private app.bolivia.swing.JCTextField lieu;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSComboMetro seance_choix;
    private com.raven.swing.TimePicker timePicker1;
    private rojerusan.RSComboMetro vehicule_choix;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables
}
