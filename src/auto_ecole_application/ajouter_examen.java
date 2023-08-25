/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author yazid
 */
public class ajouter_examen extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    ResultSet rs = null;
    Connection con = null;
    Statement st = null;
    Acceuil acceuil = new Acceuil();
    private examen_page instance;

    /**
     * Creates new form ajouter_examen
     */
    public ajouter_examen(examen_page instance) {
        initComponents();
        this.instance = instance;
        selectionner_formation();
    }

    public void type_seance() {
        String type_seance = seance_choix.getSelectedItem().toString();
        switch (type_seance) {
            case "Aucun":
                JOptionPane.showMessageDialog(null, "Vous devez choisir un type de séance !", null, JOptionPane.ERROR_MESSAGE);
                break;
            case "Code":
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
            case "Conduite":
            case "Créneau":
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
        boolean valide = true;
        try {
            java.util.Date date_cour = date_cours.getDatoFecha();
            Long date_date = date_cour.getTime();
            java.sql.Date date_cours_sql = new java.sql.Date(date_date);

            if (choix_horaire.getSelectedItem().toString().equals("Aucun") || seance_choix.getSelectedItem().toString() == "Aucun" || choix_formation.getSelectedItem().toString().equals("Aucune")) {
                JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
                valide = false;
            } else {
                if (comparer_date(date_cours_sql) <= 0) {
                    JOptionPane.showMessageDialog(null, "La date du l'examen doit être au mois supérieur d'une journée de la date actuelle !", null, JOptionPane.ERROR_MESSAGE);
                    valide = false;
                }
            }
        } catch (NullPointerException nul) {
            JOptionPane.showMessageDialog(null, "Vous devez selectionner une date", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        }
        return valide;
    }

    public int comparer_date(java.sql.Date Date_en_parametre) {
        LocalDate date_saisi = Date_en_parametre.toLocalDate();
        LocalDate date_actuel = LocalDate.now();
        Period difference = Period.between(date_actuel, date_saisi);
        int diff = difference.getDays();
        return diff;
    }

    public void selectionner_formation() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select permis from formation");
            rs = st.executeQuery();
            while (rs.next()) {
                Object permis = rs.getString(1);
                choix_formation.addItem(permis);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insert_emd() {
        if (valider_formulaire() == true) {
            try {
                int id_for = 0;
                String type = seance_choix.getSelectedItem().toString();
                String horaire = choix_horaire.getSelectedItem().toString();
                java.util.Date date_cour = date_cours.getDatoFecha();
                Long date_date = date_cour.getTime();
                java.sql.Date date_cours_sql = new java.sql.Date(date_date);
                String formation = choix_formation.getSelectedItem().toString();
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("Select id_formation from formation where permis=?");
                st.setString(1, formation);
                rs = st.executeQuery();
                if (rs.next()) {
                    id_for = rs.getInt("id_formation");
                }
                String inserer2 = "insert into examens (type,date,heure,formation_id) values (?,?,?,?)";
                PreparedStatement insert2 = con.prepareStatement(inserer2);
                insert2.setString(1, type);
                insert2.setDate(2, date_cours_sql);
                insert2.setString(3, horaire);
                insert2.setInt(4, id_for);
                insert2.executeUpdate();
                JOptionPane.showMessageDialog(null, "Examen ajouté avec succès veuillez attendre l'envoi des emails !", null, JOptionPane.INFORMATION_MESSAGE);
                PreparedStatement st2 = con.prepareStatement("select c.email,c.nom,c.prenom from candidat c ,candidat_inscrie_formation f where f.candidat_id_candidat=c.id_candidat and f.formation_id=?");
                st2.setInt(1, id_for);
                ResultSet r1 = st2.executeQuery();
                ArrayList<String> les_emails_des_candidat = new ArrayList<>();
                ArrayList<String> les_noms_des_candidat = new ArrayList<>();
                ArrayList<String> les_prenoms_des_candidat = new ArrayList<>();
                String objet = "Convocation à un examen";
                boolean reussi = true;
                while (r1.next()) {
                    les_noms_des_candidat.add(r1.getString("nom"));
                    les_prenoms_des_candidat.add(r1.getString("prenom"));
                    les_emails_des_candidat.add(r1.getString("email"));
                }
                String nom_can = "";
                String prenom_can = "";
                String email_can = "";
                String message_candidat = "";
                for (int i = 0; i < les_prenoms_des_candidat.size(); i++) {
                    email_can = les_emails_des_candidat.get(i);
                    nom_can = les_noms_des_candidat.get(i);
                    prenom_can = les_prenoms_des_candidat.get(i);
                    message_candidat = "Cher/chère candidat(e) : " + nom_can + " " + prenom_can + "\n"
                            + "\n"
                            + "Nous avons le plaisir de vous informer que vous êtes convoqué(e) à un examen  de " + type + " à l'auto-école. "
                            + "\n"
                            + "Voici les détails de la séance :\n\n\n"
                            + "\n"
                            + "Date :" + date_cours_sql + ".\n\n"
                            + "Horaire   :" + horaire + ".\n\n"
                            + "\n"
                            + "Cordialement,\n\n"
                            + "Auto école de Bejaia"
                            + "";
                    if (acceuil.EmailSender(email_can, objet, message_candidat)) {
                        System.out.println("Enovye pour " + i);
                    } else {
                        reussi = false;
                    }
                }
                if (reussi == false) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'envoi de l'email ", null, JOptionPane.ERROR_MESSAGE);

                } else {
                    this.dispose();
                    instance.selectionner_emd();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
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

        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        seance_choix = new rojerusan.RSComboMetro();
        date_cours = new rojeru_san.componentes.RSDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        choix_formation = new rojerusan.RSComboMetro();
        choix_horaire = new rojerusan.RSComboMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        kGradientPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel3.setkEndColor(new java.awt.Color(0, 102, 153));
        kGradientPanel3.setkGradientFocus(1000);
        kGradientPanel3.setkStartColor(new java.awt.Color(0, 102, 153));
        kGradientPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        kGradientPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, -1, -1));

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

        kGradientPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 71, 677, -1));

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
        kGradientPanel3.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 750, 633, 90));

        vehicule_n.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        vehicule_n.setForeground(new java.awt.Color(255, 255, 255));
        vehicule_n.setToolTipText("");
        kGradientPanel3.add(vehicule_n, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 15, -1, -1));

        id_vehicule.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        id_vehicule.setForeground(new java.awt.Color(255, 255, 255));
        id_vehicule.setToolTipText("");
        kGradientPanel3.add(id_vehicule, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 15, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Type de séance");
        kGradientPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 130, -1, -1));

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
        kGradientPanel3.add(seance_choix, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 530, -1));

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
        kGradientPanel3.add(date_cours, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 351, 530, 31));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Horaire");
        kGradientPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 450, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Date de l'examen");
        kGradientPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, -1, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Formation");
        kGradientPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 590, -1, -1));

        choix_formation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucune" }));
        choix_formation.setColorArrow(new java.awt.Color(0, 102, 153));
        choix_formation.setColorBorde(new java.awt.Color(255, 255, 255));
        choix_formation.setColorFondo(new java.awt.Color(0, 102, 153));
        choix_formation.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        choix_formation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                choix_formationItemStateChanged(evt);
            }
        });
        kGradientPanel3.add(choix_formation, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 650, 530, -1));

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
        kGradientPanel3.add(choix_horaire, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 530, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
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
        insert_emd();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void seance_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_seance_choixItemStateChanged
        // TODO add your handling code here:
        type_seance();
    }//GEN-LAST:event_seance_choixItemStateChanged

    private void choix_formationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_choix_formationItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_choix_formationItemStateChanged

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
            java.util.logging.Logger.getLogger(ajouter_examen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ajouter_examen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ajouter_examen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ajouter_examen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSComboMetro choix_formation;
    private rojerusan.RSComboMetro choix_horaire;
    private rojeru_san.componentes.RSDateChooser date_cours;
    private javax.swing.JLabel id_vehicule;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private keeptoo.KGradientPanel kGradientPanel3;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSComboMetro seance_choix;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables
}
