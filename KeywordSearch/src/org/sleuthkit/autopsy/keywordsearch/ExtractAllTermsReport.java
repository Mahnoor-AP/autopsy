/*
 * Autopsy Forensic Browser
 *
 * Copyright 2021 Basis Technology Corp.
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
package org.sleuthkit.autopsy.keywordsearch;

import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.NoCurrentCaseException;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestManager;
import org.sleuthkit.autopsy.report.GeneralReportModule;
import org.sleuthkit.autopsy.report.GeneralReportSettings;
import org.sleuthkit.autopsy.report.ReportProgressPanel;

/**
 * Instances of this class plug in to the reporting infrastructure to provide a
 * convenient way to extract all unique terms from Solr index.
 */
@ServiceProvider(service = GeneralReportModule.class)
public class ExtractAllTermsReport implements GeneralReportModule {
    
    private static final Logger logger = Logger.getLogger(ExtractAllTermsReport.class.getName());

    @NbBundle.Messages({
        "ExtractAllTermsReport.getName.text=Extract Unique Words"})
    @Override
    public String getName() {
        return Bundle.ExtractAllTermsReport_getName_text();
    }

    @NbBundle.Messages({
        "ExtractAllTermsReport.error.unableToOpenCase=Exception while getting open case.",
        "ExtractAllTermsReport.search.noFilesInIdxMsg=<html>No files are in index yet. <br />Try again later. Index is updated every {0} minutes.</html>",
        "ExtractAllTermsReport.search.noFilesInIdxMsg2=<html>No files are in index yet. <br />Try again later</html>",
        "ExtractAllTermsReport.search.searchIngestInProgressTitle=Keyword Search Ingest in Progress",
        "ExtractAllTermsReport.search.ingestInProgressBody=<html>Keyword Search Ingest is currently running.<br />Not all files have been indexed and this search might yield incomplete results.<br />Do you want to proceed with this search anyway?</html>",
        "ExtractAllTermsReport.startExport=Starting Unique Word Export",
        "ExtractAllTermsReport.export.error=Error During Unique Word Export",
        "ExtractAllTermsReport.exportComplete=Unique Word Export Complete"
    })
    @Override
    public void generateReport(GeneralReportSettings settings, ReportProgressPanel progressPanel) {
        Case openCase;
        try {
            openCase = Case.getCurrentCaseThrows();
        } catch (NoCurrentCaseException ex) {
            logger.log(Level.SEVERE, "Exception while getting open case.", ex); //NON-NLS
            progressPanel.complete(ReportProgressPanel.ReportStatus.ERROR, Bundle.ExtractAllTermsReport_error_unableToOpenCase());
            return;
        }
        progressPanel.setIndeterminate(true);
        progressPanel.start();
        progressPanel.updateStatusLabel("Extracting unique words...");

        boolean isIngestRunning = IngestManager.getInstance().isIngestRunning();

        int filesIndexed = 0;
            try { // see if another node added any indexed files
                filesIndexed = KeywordSearch.getServer().queryNumIndexedFiles();
            } catch (KeywordSearchModuleException | NoOpenCoreException ignored) {
            }

        if (filesIndexed == 0) {
            if (isIngestRunning) {
                progressPanel.complete(ReportProgressPanel.ReportStatus.ERROR, Bundle.ExtractAllTermsReport_search_noFilesInIdxMsg(KeywordSearchSettings.getUpdateFrequency().getTime()));
            } else {
                progressPanel.complete(ReportProgressPanel.ReportStatus.ERROR, Bundle.ExtractAllTermsReport_search_noFilesInIdxMsg2());
            }
            progressPanel.setIndeterminate(false);
            progressPanel.complete(ReportProgressPanel.ReportStatus.ERROR);
            return;
        }

        //check if keyword search module  ingest is running (indexing, etc)
        if (isIngestRunning) {
            if (KeywordSearchUtil.displayConfirmDialog(Bundle.ExtractAllTermsReport_search_searchIngestInProgressTitle(),
                    Bundle.ExtractAllTermsReport_search_ingestInProgressBody(), KeywordSearchUtil.DIALOG_MESSAGE_TYPE.WARN) == false) {
                return;
            }
        }

        final Server server = KeywordSearch.getServer();
        try {
            progressPanel.updateStatusLabel(Bundle.ExtractAllTermsReport_startExport());
            server.extractAllTermsForDataSource(settings, progressPanel);
        } catch (Exception ex) {
            progressPanel.updateStatusLabel(Bundle.ExtractAllTermsReport_export_error());
            Exceptions.printStackTrace(ex); // ELTODO
        }
        progressPanel.updateStatusLabel(Bundle.ExtractAllTermsReport_exportComplete());

        progressPanel.setIndeterminate(false);
        progressPanel.complete(ReportProgressPanel.ReportStatus.COMPLETE);
    }

    @Override
    public boolean supportsDataSourceSelection() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Extracts all unique words out of the current case. NOTE: The extracted words are lower-cased.";
    }

    @Override
    public String getRelativeFilePath() {
        return "Unique Words.txt"; // ELTODO
    }

}
