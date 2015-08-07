/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
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

/*
 * ImageDetailsPanel.java
 *
 * Created on May 2, 2011, 3:53:49 PM
 */
package org.sleuthkit.autopsy.directorytree;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author jantonius
 */
class ImageDetailsPanel extends javax.swing.JPanel {

    /**
     * Creates new form ImageDetailsPanel
     */
    ImageDetailsPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageInfoLabel = new javax.swing.JLabel();
        imgNameLabel = new javax.swing.JLabel();
        imgTypeLabel = new javax.swing.JLabel();
        imgSectorSizeLabel = new javax.swing.JLabel();
        imgNameValue = new javax.swing.JLabel();
        imgTypeValue = new javax.swing.JLabel();
        imgSectorSizeValue = new javax.swing.JLabel();
        OKButton = new javax.swing.JButton();
        imgTotalSizeLabel = new javax.swing.JLabel();
        imgTotalSizeValue = new javax.swing.JLabel();
        imgHashLabel = new javax.swing.JLabel();
        imgHashValue = new javax.swing.JLabel();

        imageInfoLabel.setFont(imageInfoLabel.getFont().deriveFont(Font.BOLD, 18));
        imageInfoLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imageInfoLabel.text")); // NOI18N

        imgNameLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgNameLabel.text")); // NOI18N

        imgTypeLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgTypeLabel.text")); // NOI18N

        imgSectorSizeLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgSectorSizeLabel.text")); // NOI18N

        imgNameValue.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgNameValue.text")); // NOI18N

        imgTypeValue.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgTypeValue.text")); // NOI18N

        imgSectorSizeValue.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgSectorSizeValue.text")); // NOI18N

        OKButton.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.OKButton.text")); // NOI18N

        imgTotalSizeLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgTotalSizeLabel.text")); // NOI18N

        imgTotalSizeValue.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgTotalSizeValue.text")); // NOI18N

        imgHashLabel.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgHashLabel.text")); // NOI18N

        imgHashValue.setText(org.openide.util.NbBundle.getMessage(ImageDetailsPanel.class, "ImageDetailsPanel.imgHashValue.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 68, Short.MAX_VALUE)
                .addComponent(imageInfoLabel)
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgNameLabel)
                    .addComponent(imgTypeLabel)
                    .addComponent(imgSectorSizeLabel)
                    .addComponent(imgTotalSizeLabel)
                    .addComponent(imgHashLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgNameValue)
                    .addComponent(imgTypeValue)
                    .addComponent(imgSectorSizeValue)
                    .addComponent(imgTotalSizeValue)
                    .addComponent(imgHashValue))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(imageInfoLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imgNameValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgTypeValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgSectorSizeValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgTotalSizeValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgHashValue))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(imgNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(imgTypeLabel)
                                .addGap(25, 25, 25))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(imgSectorSizeLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgTotalSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(imgHashLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(OKButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JLabel imageInfoLabel;
    private javax.swing.JLabel imgHashLabel;
    private javax.swing.JLabel imgHashValue;
    private javax.swing.JLabel imgNameLabel;
    private javax.swing.JLabel imgNameValue;
    private javax.swing.JLabel imgSectorSizeLabel;
    private javax.swing.JLabel imgSectorSizeValue;
    private javax.swing.JLabel imgTotalSizeLabel;
    private javax.swing.JLabel imgTotalSizeValue;
    private javax.swing.JLabel imgTypeLabel;
    private javax.swing.JLabel imgTypeValue;
    // End of variables declaration//GEN-END:variables

    /**
     * Sets the image name value on this panel.
     *
     * @param arg the new image name value
     */
    public void setImgNameValue(String arg) {
        imgNameValue.setText(arg);
    }

    /**
     * Sets the image type value on this panel.
     *
     * @param arg the new image type value
     */
    public void setImgTypeValue(String arg) {
        imgTypeValue.setText(arg);
    }

    /**
     * Sets the image sector size value on this panel.
     *
     * @param arg the new image size value
     */
    public void setImgSectorSizeValue(String arg) {
        imgSectorSizeValue.setText(arg);
    }

    /**
     * Sets the image size value on this panel.
     *
     * @param arg the new image size value
     */
    public void setImgTotalSizeValue(String arg) {
        imgTotalSizeValue.setText(arg);
    }

    /**
     * Sets the image hash value on this panel.
     *
     * @param arg the new image size value
     */
    public void setImgHashValue(String arg) {
        imgHashValue.setText(arg);
    }

    public void setVisibleHashInfo(boolean visible) {
        imgHashLabel.setVisible(visible);
        imgHashValue.setVisible(visible);
    }

    /**
     * Sets the OK button action listener.
     *
     * @param e the action listener
     */
    public void setOKButtonActionListener(ActionListener e) {
        OKButton.addActionListener(e);
    }
}
