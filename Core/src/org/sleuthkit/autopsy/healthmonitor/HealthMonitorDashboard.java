/*
 * Autopsy Forensic Browser
 *
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.healthmonitor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.util.Map;
import javax.swing.BoxLayout;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.openide.modules.Places;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.coreutils.MessageNotifyUtil;

/**
 * Dashboard for viewing metrics and controlling the health monitor.
 */
public class HealthMonitorDashboard {
    
    private final static Logger logger = Logger.getLogger(HealthMonitorDashboard.class.getName());
    
    private final static String ADMIN_ACCESS_FILE_NAME = "adminAccess"; // NON-NLS
    private final static String ADMIN_ACCESS_FILE_PATH = Places.getUserDirectory().getAbsolutePath() + File.separator + ADMIN_ACCESS_FILE_NAME;
    
    Map<String, List<EnterpriseHealthMonitor.DatabaseTimingResult>> timingData;

    private JComboBox dateComboBox = null;
    private JComboBox hostComboBox = null;
    private JCheckBox hostCheckBox = null;
    private JPanel graphPanel = null;
    private JDialog dialog = null;
    private final Container parentWindow;
    
    /**
     * Create an instance of the dashboard.
     * Call display() after creation to show the dashboard.
     * @param parent The parent container (for centering the UI)
     */
    public HealthMonitorDashboard(Container parent) {
        timingData = new HashMap<>();
        parentWindow = parent;
    }
    
    /**
     * Display the dashboard.
     */
    @NbBundle.Messages({"HealthMonitorDashboard.display.errorCreatingDashboard=Error creating health monitor dashboard",
                        "HealthMonitorDashboard.display.dashboardTitle=Enterprise Health Monitor"})
    public void display() {
        
        // Update the enabled status and get the timing data, then create all
        // the sub panels.
        JPanel timingPanel;
        JPanel adminPanel;        
        try {
            updateData();
            timingPanel = createTimingPanel();
            adminPanel = createAdminPanel();
        } catch (HealthMonitorException ex) {
            logger.log(Level.SEVERE, "Error creating panels for health monitor dashboard", ex);
            MessageNotifyUtil.Message.error(Bundle.HealthMonitorDashboard_display_errorCreatingDashboard());
            return;
        }
        
        // Create the main panel for the dialog
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
               
        // Put the timing data in a scroll pane and then add to the main panel
        JScrollPane scrollPane = new JScrollPane(timingPanel);
        mainPanel.add(scrollPane);      
        
        // Add the admin panel if the admin file is present
        File adminFile = new File(ADMIN_ACCESS_FILE_PATH);
        if(adminFile.exists()) {
            mainPanel.add(adminPanel);
        }
        
        // Create and show the dialog
        dialog = new JDialog();
        dialog.setTitle(Bundle.HealthMonitorDashboard_display_dashboardTitle());
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);
    }
    
    /**
     * Delete the current dialog and create a new one. This should only be
     * called after enabling or disabling the health monitor.
     */
    private void redisplay() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
        display();
    }
    
    /**
     * Check the monitor enabled status and, if enabled, get the timing data.
     * @throws HealthMonitorException 
     */
    private void updateData() throws HealthMonitorException {
        
        // Update the monitor status
        EnterpriseHealthMonitor.getInstance().updateFromGlobalEnabledStatus();
        
        if(EnterpriseHealthMonitor.monitorIsEnabled()) {
            // Get a copy of the timing data from the database
            timingData =  EnterpriseHealthMonitor.getInstance().getTimingMetricsFromDatabase(); 
        }
    }
    
    /**
     * Create the panel holding the timing graphs and the controls for them.
     * @return The timing panel
     * @throws HealthMonitorException 
     */
    @NbBundle.Messages({"HealthMonitorDashboard.createTimingPanel.noData=No data to display - monitor is not enabled",
                        "HealthMonitorDashboard.createTimingPanel.timingMetricsTitle=Timing Metrics"})
    private JPanel createTimingPanel() throws HealthMonitorException {
        JPanel timingMetricPanel = new JPanel();
        timingMetricPanel.setLayout(new BoxLayout(timingMetricPanel, BoxLayout.PAGE_AXIS));
        timingMetricPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timingMetricPanel.setBorder(BorderFactory.createEtchedBorder());
              
        // Add title
        JLabel timingMetricTitle = new JLabel(Bundle.HealthMonitorDashboard_createTimingPanel_timingMetricsTitle());
        timingMetricPanel.add(timingMetricTitle);
        timingMetricPanel.add(new JSeparator());
        
        // If the monitor isn't enabled, just add a message
        if(! EnterpriseHealthMonitor.monitorIsEnabled()) {
            timingMetricPanel.setPreferredSize(new Dimension(400,100));
            timingMetricPanel.add(new JLabel(""));
            timingMetricPanel.add(new JLabel(Bundle.HealthMonitorDashboard_createTimingPanel_noData()));
            
            timingMetricPanel.revalidate();
            timingMetricPanel.repaint();
            return timingMetricPanel;
        }
        
        // Add the controls
        timingMetricPanel.add(createTimingControlPanel());
        timingMetricPanel.add(new JSeparator());
        
        // Create panel to hold graphs
        graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.PAGE_AXIS));
        
        // Update the graph panel and add to the timing metric panel
        updateTimingMetricGraphs();
        timingMetricPanel.add(graphPanel);
        timingMetricPanel.revalidate();
        timingMetricPanel.repaint();

        return timingMetricPanel;
    }
    
    /**
     * Create the panel with combo boxes for date range and host.
     * @return the control panel
     */
    @NbBundle.Messages({"HealthMonitorDashboard.createTimingControlPanel.filterByHost=Filter by host",
                        "HealthMonitorDashboard.createTimingControlPanel.maxDays=Max days to display"})
    private JPanel createTimingControlPanel() {
        JPanel timingControlPanel = new JPanel();
        //timingControlPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // If the monitor is not enabled, don't add any components
        if(! EnterpriseHealthMonitor.monitorIsEnabled()) {
            return timingControlPanel;
        }
        
        // Create the combo box for selecting how much data to display
        String[] dateOptionStrings = Arrays.stream(DateRange.values()).map(e -> e.getLabel()).toArray(String[]::new);
        dateComboBox = new JComboBox(dateOptionStrings);
        dateComboBox.setSelectedItem(DateRange.TWO_WEEKS.getLabel());
        
        // Set up the listener on the date combo box
        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    updateTimingMetricGraphs();
                } catch (HealthMonitorException ex) {
                    logger.log(Level.SEVERE, "Error updating timing metric panel", ex);
                }
            }
        });
        
        // Create an array of host names
        Set<String> hostNameSet = new HashSet<>();
        for(String metricType:timingData.keySet()) {
            for(EnterpriseHealthMonitor.DatabaseTimingResult result: timingData.get(metricType)) {
                hostNameSet.add(result.getHostName());
            }
        }
        
        // Load the host names into the combo box
        hostComboBox = new JComboBox(hostNameSet.toArray(new String[hostNameSet.size()]));
        
        // Set up the listener on the combo box
        hostComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if((hostCheckBox != null) && hostCheckBox.isSelected()) {
                        updateTimingMetricGraphs();
                    }
                } catch (HealthMonitorException ex) {
                    logger.log(Level.SEVERE, "Error populating timing metric panel", ex);
                }
            }
        });
        
        // Create the checkbox
        hostCheckBox = new JCheckBox(Bundle.HealthMonitorDashboard_createTimingControlPanel_filterByHost());
        hostCheckBox.setSelected(false);
        hostComboBox.setEnabled(false);
        
        // Set up the listener on the checkbox
        hostCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    hostComboBox.setEnabled(hostCheckBox.isSelected());
                    updateTimingMetricGraphs();
                } catch (HealthMonitorException ex) {
                    logger.log(Level.SEVERE, "Error populating timing metric panel", ex);
                }
            }
        });
        
        // Add the date range combo box and label to the panel
        timingControlPanel.add(new JLabel(Bundle.HealthMonitorDashboard_createTimingControlPanel_maxDays()));
        timingControlPanel.add(dateComboBox);
        
        // Put some space between the elements
        timingControlPanel.add(Box.createHorizontalStrut(100));
        
        // Add the host combo box and checkbox to the panel
        timingControlPanel.add(hostCheckBox);
        timingControlPanel.add(hostComboBox);
        
        return timingControlPanel;
    }
    
    /**
     * Update the timing graphs.
     * @throws HealthMonitorException 
     */
    private void updateTimingMetricGraphs() throws HealthMonitorException {
        
        // Clear out any old graphs
        graphPanel.removeAll();
        
        for(String name:timingData.keySet()) {
            
            // If necessary, trim down the list of results to fit the selected time range
            List<EnterpriseHealthMonitor.DatabaseTimingResult> intermediateTimingDataForDisplay;
            if(dateComboBox.getSelectedItem() != null) {
                DateRange selectedDateRange = DateRange.fromLabel(dateComboBox.getSelectedItem().toString());
                if(selectedDateRange != DateRange.ALL) {
                    long threshold = System.currentTimeMillis() - selectedDateRange.getTimestampRange();
                    intermediateTimingDataForDisplay = timingData.get(name).stream()
                            .filter(t -> t.getTimestamp() > threshold)
                            .collect(Collectors.toList());
                } else {
                    intermediateTimingDataForDisplay = timingData.get(name);
                }
            } else {
                intermediateTimingDataForDisplay = timingData.get(name);
            }
            
            // Get the name of the selected host, if there is one
            String hostToDisplay = null;
            if(hostCheckBox.isSelected() && (hostComboBox.getSelectedItem() != null)) {
                hostToDisplay = hostComboBox.getSelectedItem().toString();
            }
            
            // Generate the graph
            TimingMetricGraphPanel singleTimingGraphPanel = new TimingMetricGraphPanel(intermediateTimingDataForDisplay, 
                    TimingMetricGraphPanel.TimingMetricType.AVERAGE, hostToDisplay, true);
            
            // Add the metric name and the graph to the panel
            JLabel metricNameLabel = new JLabel(name);
            graphPanel.add(metricNameLabel);
            singleTimingGraphPanel.setPreferredSize(new Dimension(900,250));
            graphPanel.add(singleTimingGraphPanel);
        }
        graphPanel.revalidate();
        graphPanel.repaint();
    }
    
    /**
     * Create the admin panel.
     * This allows the health monitor to be enabled and disabled.
     * @return 
     */
    @NbBundle.Messages({"HealthMonitorDashboard.createAdminPanel.enableButton=Enable monitor",
                        "HealthMonitorDashboard.createAdminPanel.disableButton=Disable monitor"})
    private JPanel createAdminPanel() {
        
        JPanel adminPanel = new JPanel();
        adminPanel.setBorder(BorderFactory.createEtchedBorder());

        // Create the buttons for enabling/disabling the monitor
        JButton enableButton = new JButton(Bundle.HealthMonitorDashboard_createAdminPanel_enableButton());
        JButton disableButton = new JButton(Bundle.HealthMonitorDashboard_createAdminPanel_disableButton());
        
        boolean isEnabled =  EnterpriseHealthMonitor.monitorIsEnabled();
        enableButton.setEnabled(! isEnabled);
        disableButton.setEnabled(isEnabled);

        // Set up a listener on the enable button
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    EnterpriseHealthMonitor.setEnabled(true);
                    redisplay();
                } catch (HealthMonitorException ex) {
                    logger.log(Level.SEVERE, "Error enabling monitoring", ex);
                } finally {
                    dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        
        // Set up a listener on the disable button
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    EnterpriseHealthMonitor.setEnabled(false);
                    redisplay();
                } catch (HealthMonitorException ex) {
                    logger.log(Level.SEVERE, "Error disabling monitoring", ex);
                } finally {
                    dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        
        // Add the buttons
        adminPanel.add(enableButton);
        adminPanel.add(Box.createHorizontalStrut(25));
        adminPanel.add(disableButton);
        
        return adminPanel;
    }
    
    /**
     * Possible date ranges for the metrics in the UI
     */
    @NbBundle.Messages({"HealthMonitorDashboard.DateRange.all=All",
                        "HealthMonitorDashboard.DateRange.twoWeeks=Two weeks",
                        "HealthMonitorDashboard.DateRange.oneWeek=One week"})
    private enum DateRange {
        ALL(Bundle.HealthMonitorDashboard_DateRange_all(), 0),
        TWO_WEEKS(Bundle.HealthMonitorDashboard_DateRange_twoWeeks(), 14),
        ONE_WEEK(Bundle.HealthMonitorDashboard_DateRange_oneWeek(), 7);
        
        private final String label;
        private final long numberOfDays;
        private static final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
        
        DateRange(String label, long numberOfDays) {
            this.label = label;
            this.numberOfDays = numberOfDays;
        }
        
        /**
         * Get the name for display in the UI
         * @return the name
         */
        String getLabel() {
            return label;
        }
        
        /**
         * Get the number of milliseconds represented by this date range.
         * Compare the timestamps to ((current time in millis) - (this value)) to
         * determine if they are in the range
         * @return the time range in milliseconds
         */
        long getTimestampRange() {
            if (numberOfDays > 0) {
                return numberOfDays * MILLISECONDS_PER_DAY;
            } else {
                return Long.MAX_VALUE;
            }
        }
        
        static DateRange fromLabel(String text) {
            for (DateRange dateRange : DateRange.values()) {
                if (dateRange.label.equalsIgnoreCase(text)) {
                    return dateRange;
                }
            }
            return ALL; // If the comparison failed, return a default
        }
    }
    
}
