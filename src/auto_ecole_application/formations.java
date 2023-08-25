/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yazid
 */
public final class formations extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    DefaultTableModel model, model2;

    /**
     * Creates new form formations
     */
    public formations() {
        initComponents();
        selectionner_formations();
        btn_ajouter2.setVisible(false);
        annuler.setVisible(false);
        btn_selectionner.setVisible(false);
        label_id.setVisible(false);
    }

    public void selectionner_formations() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select count(c.candidat_id_candidat) as total ,f.id_formation,f.permis,f.cout  from formation f  left join  candidat_inscrie_formation c on f.id_formation=c.formation_id group by id_formation ");
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_formation");
                String permis = rs.getString("permis");
                String cout = rs.getString("cout");
                String total = rs.getString("total");
                Object[] obj = {id, permis, cout, total};
                model = (DefaultTableModel) tableau_formation.getModel();
                model.addRow(obj);
            }
            tableau_formation.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    table1MouseReleased(evt);
                }

                private void table1MouseReleased(MouseEvent evt) {
                    int selectionner = tableau_formation.getSelectedRow();
                    String string_selectionner = String.valueOf(selectionner);
                    if (c1.est_chiffre(string_selectionner) == false) {
                        JOptionPane.showMessageDialog(null, "Aucune formation n'est selectionné !:", null, JOptionPane.ERROR_MESSAGE);
                    } else {
                        Object ids = model.getValueAt(selectionner, 0);
                        id_c.setText("" + ids);
                        id_c.setVisible(false);
                        cat.setSelectedItem(model.getValueAt(selectionner, 1).toString());
                        cout.setText(model.getValueAt(selectionner, 2).toString());
                        btn_ajouter2.setVisible(true);
                        form_ajouter.setVisible(true);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void inserer_formations() {
        String categorie = cat.getSelectedItem().toString();
        String prix = cout.getText();
        if (!c1.est_chiffre(prix) || prix.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pour le coût il faut choisir une valeur numérique et positif !:", null, JOptionPane.ERROR_MESSAGE);
        } else {
            int prix_int = Integer.parseInt(prix);
            try {
                con = c1.connection_db();
                String sql = "insert into formation(permis,cout) values (?,?)";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, categorie);
                st.setInt(2, prix_int);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Une formation ajoutée avec succès !", null, JOptionPane.INFORMATION_MESSAGE);
                model.setRowCount(0);
                selectionner_formations();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void selectionner_candidats_sans_formation() {
        model2 = (DefaultTableModel) tableau_candidat.getModel();
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("SELECT * from candidat c where c.id_candidat not in (SELECT cf.candidat_id_candidat FROM candidat_inscrie_formation cf) and c.groupe is not null ");
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_candidat");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date naissance = rs.getDate("date_naissance");
                Object[] obj = {id, nom, prenom, naissance};
                model2.addRow(obj);
            }
            tableau_candidat.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    table1MouseReleased(evt);
                }
                private void table1MouseReleased(MouseEvent evt) {
                    int selectionner = tableau_candidat.getSelectedRow();
                    String string_selectionner = String.valueOf(selectionner);
                    if (c1.est_chiffre(string_selectionner) == false) {
                        JOptionPane.showMessageDialog(null, "Aucun candidat n'est selectionné !", null, JOptionPane.ERROR_MESSAGE);
                    } else {
                        Object ids = model2.getValueAt(selectionner, 0);
                        label_id.setText(ids.toString());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ajouter_a_formation(String l_id) {
        try {
            con = c1.connection_db();
            String sql = "insert into candidat_inscrie_formation (candidat_id_candidat,formation_id) values (?,?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, label_id.getText());
            st.setString(2, id_c.getText());
            st.executeUpdate();
            model.setRowCount(0);
            selectionner_formations();
            model2.setRowCount(0);
            selectionner_candidats_sans_formation();
            id_c.setText("");
            label_id.setText("");
            JOptionPane.showMessageDialog(null, "Le candidat a été ajouté à la formation Numéro " + id_c.getText(), null, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        id_veh = new javax.swing.JLabel();
        titre_veh = new javax.swing.JLabel();
        id_c2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableau_formation = new rojerusan.RSTableMetro();
        jLabel21 = new javax.swing.JLabel();
        id_c = new javax.swing.JLabel();
        form_ajouter = new keeptoo.KGradientPanel();
        jLabel8 = new javax.swing.JLabel();
        btn_ajouter = new rojeru_san.complementos.RSButtonHover();
        cat = new rojerusan.RSComboMetro();
        jLabel9 = new javax.swing.JLabel();
        cout = new app.bolivia.swing.JCTextField();
        jLabel11 = new javax.swing.JLabel();
        btn_modifer = new rojeru_san.complementos.RSButtonHover();
        btn_supprimer = new rojeru_san.complementos.RSButtonHover();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        annuler = new rojeru_san.complementos.RSButtonHover();
        btn_selectionner = new rojeru_san.complementos.RSButtonHover();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableau_candidat = new rojerusan.RSTableMetro();
        btn_ajouter2 = new rojeru_san.complementos.RSButtonHover();
        label_id = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 3, 3, 3, new java.awt.Color(0, 102, 153)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 3, 0, 3, new java.awt.Color(0, 102, 153)));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setText("Liste des formations ");

        id_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        titre_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        id_c2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(titre_veh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_veh))
                    .addComponent(id_c2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel19)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id_veh)
                    .addComponent(titre_veh))
                .addGap(40, 40, 40))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(id_c2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 10, 2, new java.awt.Color(0, 102, 153)));

        tableau_formation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id formation", "Catègorie Permis", "Coût (DA)", "Nombre d'étudiant"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_formation.setToolTipText("");
        tableau_formation.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_formation.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_formation.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_formation.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_formation.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_formation.setFillsViewportHeight(true);
        tableau_formation.setFocusCycleRoot(true);
        tableau_formation.setFuenteFilas(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_formation.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_formation.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableau_formation.setRowHeight(50);
        jScrollPane3.setViewportView(tableau_formation);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 30, Short.MAX_VALUE))
        );

        id_c.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N

        form_ajouter.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 51, 255)));
        form_ajouter.setkEndColor(new java.awt.Color(0, 102, 153));
        form_ajouter.setkStartColor(new java.awt.Color(102, 102, 102));

        jLabel8.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("AJOUTER / MODIFIER");

        btn_ajouter.setBackground(new java.awt.Color(0, 102, 153));
        btn_ajouter.setText("AJOUTER");
        btn_ajouter.setColorHover(new java.awt.Color(255, 255, 255));
        btn_ajouter.setColorTextHover(new java.awt.Color(0, 102, 153));
        btn_ajouter.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btn_ajouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajouterMouseClicked(evt);
            }
        });

        cat.setForeground(new java.awt.Color(0, 0, 0));
        cat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "A1", "B", "B1", "F", "C", "CA", "D", "D1E" }));
        cat.setColorArrow(new java.awt.Color(0, 0, 0));
        cat.setColorBorde(new java.awt.Color(51, 51, 255));
        cat.setColorFondo(new java.awt.Color(255, 255, 255));
        cat.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                catItemStateChanged(evt);
            }
        });
        cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                catMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Catègorie Permis");

        cout.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        cout.setPlaceholder("Le prix de la formation en DA");

        jLabel11.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Le coût ");

        btn_modifer.setBackground(new java.awt.Color(0, 102, 153));
        btn_modifer.setText("MODIFIER");
        btn_modifer.setColorHover(new java.awt.Color(0, 255, 0));
        btn_modifer.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btn_modifer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_modiferMouseClicked(evt);
            }
        });

        btn_supprimer.setBackground(new java.awt.Color(0, 102, 153));
        btn_supprimer.setColorHover(new java.awt.Color(255, 51, 51));
        btn_supprimer.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btn_supprimer.setLabel("SUPPRIMER");
        btn_supprimer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_supprimerMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout form_ajouterLayout = new javax.swing.GroupLayout(form_ajouter);
        form_ajouter.setLayout(form_ajouterLayout);
        form_ajouterLayout.setHorizontalGroup(
            form_ajouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(form_ajouterLayout.createSequentialGroup()
                .addGroup(form_ajouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(form_ajouterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_modifer, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_supprimer, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(form_ajouterLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(form_ajouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(cout, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        form_ajouterLayout.setVerticalGroup(
            form_ajouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(form_ajouterLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel8)
                .addGap(106, 106, 106)
                .addComponent(jLabel9)
                .addGap(40, 40, 40)
                .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127)
                .addComponent(jLabel11)
                .addGap(27, 27, 27)
                .addComponent(cout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                .addGroup(form_ajouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_modifer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_supprimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );

        kGradientPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 0, 3, new java.awt.Color(255, 255, 255)));
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel2.setkGradientFocus(2500);
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\lib\\AddNewBookIcons\\icons8_Rewind_48px.png")); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Gestion des formations");

        rSButtonHover3.setBackground(new java.awt.Color(0, 102, 153));
        rSButtonHover3.setText("X");
        rSButtonHover3.setColorHover(new java.awt.Color(255, 255, 255));
        rSButtonHover3.setColorTextHover(new java.awt.Color(0, 102, 153));
        rSButtonHover3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSButtonHover3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSButtonHover3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        annuler.setBackground(new java.awt.Color(255, 0, 0));
        annuler.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 255)));
        annuler.setActionCommand("Ajouter des candidat");
        annuler.setColorHover(new java.awt.Color(255, 255, 255));
        annuler.setColorTextHover(new java.awt.Color(0, 102, 153));
        annuler.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        annuler.setLabel("Annuler");
        annuler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annulerMouseClicked(evt);
            }
        });
        annuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerActionPerformed(evt);
            }
        });

        btn_selectionner.setBackground(new java.awt.Color(0, 102, 153));
        btn_selectionner.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 255)));
        btn_selectionner.setText("Ajouter ");
        btn_selectionner.setToolTipText("");
        btn_selectionner.setActionCommand("Ajouter des candidat");
        btn_selectionner.setColorHover(new java.awt.Color(255, 255, 255));
        btn_selectionner.setColorTextHover(new java.awt.Color(0, 102, 153));
        btn_selectionner.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_selectionner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_selectionnerMouseClicked(evt);
            }
        });
        btn_selectionner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_selectionnerActionPerformed(evt);
            }
        });

        tableau_candidat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id ", "Nom", "Prenom", "Date naissance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_candidat.setToolTipText("");
        tableau_candidat.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_candidat.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_candidat.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_candidat.setFillsViewportHeight(true);
        tableau_candidat.setFocusCycleRoot(true);
        tableau_candidat.setFuenteFilas(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableau_candidat.setRowHeight(50);
        jScrollPane4.setViewportView(tableau_candidat);

        btn_ajouter2.setBackground(new java.awt.Color(0, 102, 153));
        btn_ajouter2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 255)));
        btn_ajouter2.setText("Ajouter des candidats");
        btn_ajouter2.setActionCommand("Ajouter des candidat");
        btn_ajouter2.setColorHover(new java.awt.Color(255, 255, 255));
        btn_ajouter2.setColorTextHover(new java.awt.Color(0, 102, 153));
        btn_ajouter2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_ajouter2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajouter2MouseClicked(evt);
            }
        });

        label_id.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_ajouter2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(id_c)
                        .addGap(733, 733, 733)
                        .addComponent(jLabel21)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(form_ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label_id)
                                .addGap(619, 619, 619))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(annuler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btn_selectionner, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(24, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(label_id)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 39, Short.MAX_VALUE)
                                .addComponent(id_c)
                                .addGap(31, 31, 31))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_ajouter2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annuler, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_selectionner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(form_ajouter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jLabel21)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSButtonHover3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonHover3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_rSButtonHover3MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseClicked

    private void btn_supprimerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_supprimerMouseClicked
        // TODO add your handling code here:
        if (id_c.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez selectionner une formation  !", null, JOptionPane.ERROR_MESSAGE);
        } //JOptionPane.showConfirmDialog(null,"Etes vous sur de supprimer cette formation ? ", "Suppression d'une formation",JOptionPane.YES_NO_OPTION);
        else {
            if (JOptionPane.showConfirmDialog(null, "Etes vous sur de supprimer cette formation ? ", "Suppression d'une formation", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    con = c1.connection_db();
                    PreparedStatement pst = con.prepareStatement("delete from formation where id_formation =?");
                    pst.setString(1, id_c.getText());
                    int row_deleted = pst.executeUpdate();
                    if (row_deleted > 0) {
                        JOptionPane.showMessageDialog(null, "Une formation est supprimée avec succès  !", null, JOptionPane.INFORMATION_MESSAGE);
                        model.setRowCount(0);
                        selectionner_formations();
                    } else {
                        JOptionPane.showMessageDialog(null, "Une erreur lors de la suppression réssayer !", null, JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLIntegrityConstraintViolationException unique) {
                    JOptionPane.showMessageDialog(null, "Vous pouvez pas supprimer la formation !", null, JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                    System.out.println("" + ex.getClass().getSimpleName());
                }
            }
        }
    }//GEN-LAST:event_btn_supprimerMouseClicked

    private void catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_catMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_catMouseClicked

    private void catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_catItemStateChanged

    }//GEN-LAST:event_catItemStateChanged

    private void btn_ajouterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajouterMouseClicked
        // TODO add your handling code here:
        inserer_formations();
    }//GEN-LAST:event_btn_ajouterMouseClicked

    private void btn_ajouter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajouter2MouseClicked
        btn_ajouter2.setVisible(false);
        annuler.setVisible(true);
        btn_selectionner.setVisible(true);
        selectionner_candidats_sans_formation();
    }//GEN-LAST:event_btn_ajouter2MouseClicked

    private void annulerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annulerMouseClicked
        // TODO add your handling code here:
        btn_ajouter2.setVisible(false);
        annuler.setVisible(false);
        btn_selectionner.setVisible(false);
        model2.setRowCount(0);
    }//GEN-LAST:event_annulerMouseClicked

    private void annulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_annulerActionPerformed

    private void btn_selectionnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectionnerMouseClicked
        // TODO add your handling code here:
        if (label_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné", null, JOptionPane.ERROR_MESSAGE);
        } else {
            if (id_c.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vous devez selectionner la formation a laquelle vous souhaitez ajouter ce candidat", null, JOptionPane.ERROR_MESSAGE);
            } else {
                ajouter_a_formation(label_id.getText());
            }
        }
    }//GEN-LAST:event_btn_selectionnerMouseClicked

    private void btn_selectionnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_selectionnerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_selectionnerActionPerformed

    private void btn_modiferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_modiferMouseClicked
        // TODO add your handling code here:
        if (id_c.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucune formation n'est selectionnée", null, JOptionPane.ERROR_MESSAGE);
        } else {
            if (cout.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir le champs prix", null, JOptionPane.ERROR_MESSAGE);
            } else {
                int prix_int = Integer.parseInt(cout.getText());

                try {
                    con = c1.connection_db();
                    String sql = "update  formation set permis =? ,cout=? where id_formation=? ";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.setString(1, cat.getSelectedItem().toString());
                    st.setInt(2, prix_int);
                    st.setString(3, id_c.getText());
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Formation modifée avec succèes ", null, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_modiferMouseClicked

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
            java.util.logging.Logger.getLogger(formations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formations().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.complementos.RSButtonHover annuler;
    private rojeru_san.complementos.RSButtonHover btn_ajouter;
    private rojeru_san.complementos.RSButtonHover btn_ajouter2;
    private rojeru_san.complementos.RSButtonHover btn_modifer;
    private rojeru_san.complementos.RSButtonHover btn_selectionner;
    private rojeru_san.complementos.RSButtonHover btn_supprimer;
    private rojerusan.RSComboMetro cat;
    private app.bolivia.swing.JCTextField cout;
    private keeptoo.KGradientPanel form_ajouter;
    private javax.swing.JLabel id_c;
    private javax.swing.JLabel id_c2;
    private javax.swing.JLabel id_veh;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel label_id;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    private rojerusan.RSTableMetro tableau_candidat;
    private rojerusan.RSTableMetro tableau_formation;
    private javax.swing.JLabel titre_veh;
    // End of variables declaration//GEN-END:variables
}
