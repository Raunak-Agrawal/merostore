package com.merostore.backend.utils.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleAnalyticsService {
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "/Users/rauagrawal/Downloads/merostore-500fd-2f1f5b08717d.json";
    private static final String VIEW_ID = "257528741";

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        ServiceAccountCredentials serviceAccountCredentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(KEY_FILE_LOCATION));

        GoogleCredentials googleCredentials = serviceAccountCredentials
                .createScoped(Collections.singletonList(AnalyticsReportingScopes.ANALYTICS_READONLY));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(googleCredentials);

        // Construct the Analytics Reporting service object.
        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private static GetReportsResponse getReport(AnalyticsReporting service, String slug) throws IOException {
        // Create the DateRange object.
        DateRange dateRange1 = new DateRange();
        dateRange1.setStartDate("2021-12-20"); //the date in which app was started.
        dateRange1.setEndDate("today");

        DateRange dateRange2 = new DateRange();
        dateRange2.setStartDate("today");
        dateRange2.setEndDate("today");

        // Create the Metrics object.
        Metric pageviews = new Metric()
                .setExpression("ga:pageviews")
                .setAlias("pageviews");

        Dimension pagePath = new Dimension().setName("ga:pagePath");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange1, dateRange2))
                .setMetrics(Arrays.asList(pageviews))
                .setDimensions(Arrays.asList(pagePath)).setFiltersExpression("ga:pagePath==" + slug);

        ArrayList<ReportRequest> requests = new ArrayList<>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    public static String getResponse(String slug){
        try {
            AnalyticsReporting service = initializeAnalyticsReporting();
            GetReportsResponse response = getReport(service , slug);
            log.info("Response:{}", response);
            return response.getReports().get(0).toString();
        } catch (Exception e) {
            log.error("Error occured google analytics service :{}", e);
            throw new RuntimeException("Unable to get metadata response");
        }

    }


//    private static void printResponse(GetReportsResponse response) {
//
//        for (Report report: response.getReports()) {
//            ColumnHeader header = report.getColumnHeader();
//            List<String> dimensionHeaders = header.getDimensions();
//            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
//            List<ReportRow> rows = report.getData().getRows();
//
//            if (rows == null) {
//                System.out.println("No data found for " + VIEW_ID);
//                return;
//            }
//
//            for (ReportRow row: rows) {
//                List<String> dimensions = row.getDimensions();
//                List<DateRangeValues> metrics = row.getMetrics();
//
//                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
//                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
//                }
//
//                for (int j = 0; j < metrics.size(); j++) {
//                    System.out.print("Date Range (" + j + "): ");
//                    DateRangeValues values = metrics.get(j);
//                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
//                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
//                    }
//                }
//            }
//        }
//    }
}
