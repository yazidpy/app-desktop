/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;

/**
 *
 * @author yazid
 */
public final class espace_admin extends javax.swing.JFrame {

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    Candidat c1 = new Candidat();
   // String ids_con;
    private final String ids_con;

    /**
     * Creates new form espace_adm
     * @param ids_con
     */
    public espace_admin(String ids_con) {
        initComponents();
        this.ids_con = ids_con;
        LocalDate date_de_jour = LocalDate.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_actuell_maintenant = date_de_jour.format(formater);
        String day = date_de_jour.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        date_actuelle.setText(date_actuell_maintenant);
        jour_actuel.setText(day);
        recup_nom(ids_con);
        statistique();
    }

    public void recup_nom(String ids) {
        String type = "";
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from employe where id_employe=?");
            st.setString(1, ids);
            rs = st.executeQuery();
            while (rs.next()) {
                String nom = rs.getString("nom");
                String Prenom = rs.getString("prenom");
                admin_nom.setText(nom + " " + Prenom);
                type = rs.getString("type");
            }

            if (type.equals("Moniteur")) {
                label1.setVisible(false);
                label2.setVisible(false);
                label3.setVisible(false);
                label4.setVisible(false);
                label6.setVisible(false);
                label7.setVisible(false);
                label9.setVisible(false);
            } else {
                dessiner_bar();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);

        }
    }

    public void statistique() {
        try {
            con = c1.connection_db();
            String query1 = "SELECT COUNT(*) FROM seance";
            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query1);
            rs1.next();
            int count1 = rs1.getInt(1);
            label_cours.setText("" + count1);
            String query2 = "SELECT COUNT(*) FROM candidat";
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            rs2.next();
            int count2 = rs2.getInt(1);
            label_can.setText("" + count2);
            String query3 = "SELECT COUNT(*) FROM vehicule";
            Statement stmt3 = con.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query3);
            rs3.next();
            int count3 = rs3.getInt(1);
            label_vehi.setText("" + count3);
            String query4 = "SELECT COUNT(*) FROM groupe";
            Statement stmt4 = con.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            rs4.next();
            int count4 = rs4.getInt(1);
            label_grp.setText("" + count4);
            String query5 = "SELECT COUNT(*) FROM employe";
            Statement stmt5 = con.createStatement();
            ResultSet rs5 = stmt4.executeQuery(query5);
            rs5.next();
            int count5 = rs5.getInt(1);
            label_emp.setText("" + count5);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dessiner_cercle() {
        DefaultPieDataset data_cercle = new DefaultPieDataset();
        data_cercle.setValue("Critères 1", Double.valueOf(20));
        data_cercle.setValue("Critères 2", Double.valueOf(40));
        data_cercle.setValue("Critères 3", Double.valueOf(40));
        data_cercle.setValue("Critères 4", Double.valueOf(10));
        data_cercle.setValue("Critères 5", Double.valueOf(10));
        data_cercle.setValue("Critères 6", Double.valueOf(10));
        JFreeChart piechart = ChartFactory.createPieChart("Cercle", data_cercle, false, true, true);
        PiePlot plot = (PiePlot) piechart.getPlot();
        plot.setSectionPaint("Critères 1", new Color(255, 255, 102));
        plot.setSectionPaint("Critères 2", new Color(102, 255, 102));
        plot.setSectionPaint("Critères 3", new Color(255, 102, 153));
        plot.setSectionPaint("Critères 4", new Color(0, 204, 204));
        plot.setSectionPaint("Critères 5", new Color(0, 105, 98));
        plot.setSectionPaint("Critères 5", new Color(100, 243, 154));
        plot.setBackgroundPaint(Color.white);
        ChartPanel barpanel = new ChartPanel(piechart);
        cercle.removeAll();
        cercle.add(barpanel, BorderLayout.CENTER);
        cercle.validate();
    }

    public void dessiner_bar() {
        int somme_des_employes = 0;
        ArrayList<Integer> les_sommes_par_mois = new ArrayList<>();

        try {
            PreparedStatement formation_nom = con.prepareStatement("select sum(salaire_mensuel) as total_salaire from employe");
            ResultSet rs2 = formation_nom.executeQuery();
            if (rs2.next()) {
                somme_des_employes = rs2.getInt("total_salaire");
            }
            for (int i = 1; i <= 12; i++) {
                PreparedStatement mois = con.prepareStatement("select sum(montant) as total_montant from paiment where Month(date)=?");
                mois.setInt(1, i);
                ResultSet rm = mois.executeQuery();
                while (rm.next()) {
                    les_sommes_par_mois.add(rm.getInt("total_montant"));
                }
            }
            System.out.print("" + les_sommes_par_mois);

            // JOptionPane.showMessageDialog(null, "Erreur  :" + somme_des_employes, null, JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        DefaultCategoryDataset data_org = new DefaultCategoryDataset();
        data_org.setValue(les_sommes_par_mois.get(0), "valeur 1", "Jan");
        data_org.setValue(les_sommes_par_mois.get(1), "valeur 2", "Fév");
        data_org.setValue(les_sommes_par_mois.get(2), "valeur 3", "Mars");
        data_org.setValue(les_sommes_par_mois.get(3), "valeur 4", "Avril");
        data_org.setValue(les_sommes_par_mois.get(4), "valeur 5", "Mai");
        data_org.setValue(les_sommes_par_mois.get(5), "valeur 6", "Juin");
        data_org.setValue(les_sommes_par_mois.get(6), "valeur 7", "Juillet");
        data_org.setValue(les_sommes_par_mois.get(7), "valeur 8", "Août");
        data_org.setValue(les_sommes_par_mois.get(8), "valeur 9", "Sept");
        data_org.setValue(les_sommes_par_mois.get(9), "valeur 10", "Oct");
        data_org.setValue(les_sommes_par_mois.get(10), "valeur 11", "Nov");
        data_org.setValue(les_sommes_par_mois.get(11), "valeur 12", "Dec");
        JFreeChart chart = ChartFactory.createBarChart("Revenu de l'Auto école par mois", "Mois", "Prix(DA)", data_org, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot catplot = chart.getCategoryPlot();
        catplot.setBackgroundPaint(Color.white);
        BarRenderer rend = (BarRenderer) catplot.getRenderer();
        Color clr = new Color(204, 32, 32);
        rend.setSeriesPaint(0, clr);
        ChartPanel barpanel = new ChartPanel(chart);
        cercle.removeAll();
        cercle.add(barpanel, BorderLayout.CENTER);
        cercle.validate();
    }

    public void dessiner_histogram() {
        double[] valeurs = {10, 3, 90, 91, 2, 31, 92, 21, 55, 12, 45, 65, 89, 90, 2, 12, 77, 24, 89, 90, 14, 46, 56, 10, 90, 2};
        HistogramDataset data_histo = new HistogramDataset();
        data_histo.addSeries("key", valeurs, 20);
        JFreeChart chart = ChartFactory.createHistogram("Dépenses Par moi", "Mois", "Prix (DA)", data_histo, PlotOrientation.VERTICAL, false, true, false);
        XYPlot xplot = chart.getXYPlot();
        xplot.setBackgroundPaint(Color.white);
        ChartPanel barpanel = new ChartPanel(chart);
        cercle.removeAll();
        cercle.add(barpanel, BorderLayout.CENTER);
        cercle.validate();
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
        jLabel2 = new javax.swing.JLabel();
        admin_nom = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        date_actuelle = new javax.swing.JLabel();
        jour_actuel = new javax.swing.JLabel();
        menu = new javax.swing.JLabel();
        panel_menuu = new keeptoo.KGradientPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label6 = new javax.swing.JLabel();
        label7 = new javax.swing.JLabel();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        label8 = new javax.swing.JLabel();
        label9 = new javax.swing.JLabel();
        label10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        label_can = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        label_cours = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        label_emp = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        label_vehi = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        label_grp = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        cercle = new keeptoo.KGradientPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(51, 51, 51));
        kGradientPanel1.setkGradientFocus(1500);
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel2.setBackground(new java.awt.Color(102, 255, 255));
        jLabel2.setFont(new java.awt.Font("Algerian", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Système d'une auto école");

        admin_nom.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        admin_nom.setForeground(new java.awt.Color(255, 255, 255));
        admin_nom.setText("Admin");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/male_user_50px.png"))); // NOI18N
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        date_actuelle.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        date_actuelle.setForeground(new java.awt.Color(255, 255, 255));
        date_actuelle.setText("29/20/2001");

        jour_actuel.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jour_actuel.setForeground(new java.awt.Color(255, 255, 255));
        jour_actuel.setText("Samedi");

        menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_menu_48px_1.png"))); // NOI18N
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menu)
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jour_actuel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date_actuelle)
                .addGap(540, 540, 540)
                .addComponent(admin_nom)
                .addGap(28, 28, 28)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(menu))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(admin_nom)
                            .addComponent(date_actuelle)
                            .addComponent(jour_actuel))))
                .addContainerGap(17, Short.MAX_VALUE))
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panel_menuu.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 5, 5, 10, new java.awt.Color(255, 255, 255)));
        panel_menuu.setkEndColor(new java.awt.Color(51, 51, 51));
        panel_menuu.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel11.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tableau de bord");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("------------------------");

        label1.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("candidats");
        label1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label1MouseClicked(evt);
            }
        });

        label2.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Employes");
        label2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label2MouseClicked(evt);
            }
        });

        label3.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("Vehicules");
        label3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label3MouseClicked(evt);
            }
        });

        label4.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label4.setForeground(new java.awt.Color(255, 255, 255));
        label4.setText("Groupes");
        label4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label4MouseClicked(evt);
            }
        });

        label6.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label6.setForeground(new java.awt.Color(255, 255, 255));
        label6.setText("Formations");
        label6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label6MouseClicked(evt);
            }
        });

        label7.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label7.setForeground(new java.awt.Color(255, 255, 255));
        label7.setText("Séances ");
        label7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label7MouseClicked(evt);
            }
        });

        kGradientPanel3.setkEndColor(new java.awt.Color(51, 51, 51));
        kGradientPanel3.setkGradientFocus(250);
        kGradientPanel3.setkStartColor(new java.awt.Color(0, 102, 153));
        kGradientPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kGradientPanel3MouseClicked(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("déconnexion");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Exit_26px_2.png"))); // NOI18N

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel1))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        label8.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label8.setForeground(new java.awt.Color(255, 255, 255));
        label8.setText("Examens");
        label8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label8MouseClicked(evt);
            }
        });

        label9.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label9.setForeground(new java.awt.Color(255, 255, 255));
        label9.setText("Paiement");
        label9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label9MouseClicked(evt);
            }
        });

        label10.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        label10.setForeground(new java.awt.Color(255, 255, 255));
        label10.setText("Planinng");
        label10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panel_menuuLayout = new javax.swing.GroupLayout(panel_menuu);
        panel_menuu.setLayout(panel_menuuLayout);
        panel_menuuLayout.setHorizontalGroup(
            panel_menuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_menuuLayout.createSequentialGroup()
                .addGroup(panel_menuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_menuuLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel11))
                    .addGroup(panel_menuuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_menuuLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(panel_menuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label2)
                            .addComponent(label1)
                            .addGroup(panel_menuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(label4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(label6)
                            .addComponent(label7)
                            .addComponent(label8)
                            .addComponent(label9)
                            .addComponent(label10))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panel_menuuLayout.setVerticalGroup(
            panel_menuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menuuLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel11)
                .addGap(34, 34, 34)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label1)
                .addGap(31, 31, 31)
                .addComponent(label2)
                .addGap(38, 38, 38)
                .addComponent(label3)
                .addGap(44, 44, 44)
                .addComponent(label4)
                .addGap(41, 41, 41)
                .addComponent(label6)
                .addGap(40, 40, 40)
                .addComponent(label7)
                .addGap(44, 44, 44)
                .addComponent(label8)
                .addGap(40, 40, 40)
                .addComponent(label9)
                .addGap(34, 34, 34)
                .addComponent(label10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 3, new java.awt.Color(0, 102, 153)));

        jLabel29.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 102, 153));
        jLabel29.setText("TOTALE");

        jLabel28.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel28.setText("Candidats");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 1, 1, 1, new java.awt.Color(0, 102, 153)));

        label_can.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        label_can.setText("5");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel32)
                .addGap(18, 18, 18)
                .addComponent(label_can, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_can)
                    .addComponent(jLabel32))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel30.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel30.setText("Cours");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 1, 1, 1, new java.awt.Color(0, 102, 153)));

        label_cours.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        label_cours.setText("5");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addComponent(label_cours, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_cours)
                    .addComponent(jLabel34))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 1, 1, 1, new java.awt.Color(0, 102, 153)));

        label_emp.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        label_emp.setText("5");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addComponent(label_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_emp)
                    .addComponent(jLabel36))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 1, 1, 1, new java.awt.Color(0, 102, 153)));

        label_vehi.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        label_vehi.setText("5");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addComponent(label_vehi, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_vehi)
                    .addComponent(jLabel38))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 1, 1, 1, new java.awt.Color(0, 102, 153)));

        label_grp.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        label_grp.setText("5");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addComponent(label_grp, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_grp)
                    .addComponent(jLabel40))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel42.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel42.setText("Employes");

        jLabel43.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel43.setText("Vehicules");

        jLabel44.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jLabel44.setText("Groupe");

        cercle.setkEndColor(new java.awt.Color(255, 255, 255));
        cercle.setkStartColor(new java.awt.Color(204, 255, 255));
        cercle.setPreferredSize(new java.awt.Dimension(600, 400));
        cercle.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(232, 232, 232)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addGap(192, 192, 192)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addGap(187, 187, 187)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(723, 723, 723))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(382, 382, 382)
                .addComponent(cercle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel30)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86)
                .addComponent(cercle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_menuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_menuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void label1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label1MouseClicked
        // TODO add your handling code here:
        Candidat candidats = new Candidat();
        candidats.setVisible(true);
    }//GEN-LAST:event_label1MouseClicked

    private void label2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label2MouseClicked
        // TODO add your handling code here:

        Employe m1 = new Employe();
        m1.setVisible(true);
    }//GEN-LAST:event_label2MouseClicked

    private void label3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label3MouseClicked
        // TODO add your handling code here:
        Vehicules va = new Vehicules();
        va.setVisible(true);
    }//GEN-LAST:event_label3MouseClicked

    private void kGradientPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel3MouseClicked
        // TODO add your handling code here:
        Acceuil Ac = new Acceuil();
        Ac.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_kGradientPanel3MouseClicked

    private void label9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label9MouseClicked
        // TODO add your handling code here:
        paiment p = new paiment();
        p.setVisible(true);
    }//GEN-LAST:event_label9MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel7MouseClicked

    private void label4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label4MouseClicked
        // TODO add your handling code here:
        groupe g = new groupe();
        g.setVisible(true);
    }//GEN-LAST:event_label4MouseClicked

    private void menuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_menuMouseClicked

    private void label8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label8MouseClicked
        // TODO add your handling code here:
        examen_page ex = new examen_page();
        ex.setVisible(true);

    }//GEN-LAST:event_label8MouseClicked

    private void label6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label6MouseClicked
        // TODO add your handling code here:
        formations f = new formations();
        f.setVisible(true);

    }//GEN-LAST:event_label6MouseClicked

    private void label10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label10MouseClicked
        // TODO add your handling code here:
        planning p = new planning();
        p.setVisible(true);
    }//GEN-LAST:event_label10MouseClicked

    private void label7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label7MouseClicked
        // TODO add your handling code here:
        cours_app ap = new cours_app();
        ap.setVisible(true);
    }//GEN-LAST:event_label7MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel15MouseClicked

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
            java.util.logging.Logger.getLogger(espace_admin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(espace_admin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(espace_admin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(espace_admin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel admin_nom;
    private keeptoo.KGradientPanel cercle;
    private javax.swing.JLabel date_actuelle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jour_actuel;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel3;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JLabel label_can;
    private javax.swing.JLabel label_cours;
    private javax.swing.JLabel label_emp;
    private javax.swing.JLabel label_grp;
    private javax.swing.JLabel label_vehi;
    private javax.swing.JLabel menu;
    private keeptoo.KGradientPanel panel_menuu;
    // End of variables declaration//GEN-END:variables
}
