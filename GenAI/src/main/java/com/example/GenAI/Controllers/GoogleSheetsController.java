package com.example.GenAI.Controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDate;

import com.example.GenAI.model.Data;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import com.google.api.client.util.store.FileDataStoreFactory;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;


@RestController
@RequestMapping("/api/export")
public class GoogleSheetsController {
	
	
	private GoogleAuthorizationCodeFlow flow;
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    //properties for google sheets
    private final String clientId = ""; //write yours
    private final String clientSecret = ""; //write yours 
    private final String redirectUri = "http://localhost:8080/api/export/oauth2callback";
    
    List<Data> dataToExport = new ArrayList<Data>();
    
    @PostConstruct
    public void init() throws IOException, GeneralSecurityException {
        try {
            //System.out.println("Initializing with hardcoded credentials");
            
        	// create http transport
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            //System.out.println("HTTP Transport initialized successfully");
            
            // Build client secrets
            GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
            GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
            details.setClientId(clientId);
            details.setClientSecret(clientSecret);
            clientSecrets.setWeb(details);

            //System.out.println("Client secrets configured");
            
            // Initialize the flow
            flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setAccessType("offline")
                .build();
            
            //System.out.println("Flow initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Initialization failed:");
            e.printStackTrace();
            throw e;
        }
    }
    
    @GetMapping("/auth")
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {
        
    	/* Crete the OAuth2 url and redirect the user to google page*/
    	
    	String url = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setAccessType("offline")
                .build();
        response.sendRedirect(url);
    }
    
    
    /*This is redirect link after authentication*/
    @GetMapping("/oauth2callback")
    public void oauth2Callback(HttpSession session, @RequestParam("code") String code, 
                             HttpServletResponse response) throws IOException, GeneralSecurityException {
        
    	this.dataToExport = (List<Data>) session.getAttribute("extData");
    	
    	//test
//    	for(Data chData: dataToExport) {
//    		System.out.println("Data: "+ chData.getSource());
//    	}
    	//end test
    	
    	// Exchange code for tokens
    	// send code back to google to verify the code and redirect links matching
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        Credential credential = flow.createAndStoreCredential(tokenResponse, null);

        
        
        // Create Sheets service
        Sheets sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName("GenAI")
                .build();

        // Create new spreadsheet
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle("Exported Data " + LocalDate.now()+"_"+LocalTime.now()));

        Spreadsheet createdSheet = sheetsService.spreadsheets().create(spreadsheet).execute();

        List<List<Object>> values = new ArrayList<>();
        
        // Headers
        values.add(Arrays.asList(
            "Type", "Source", "Date", "Client_Name", "Email", 
            "Phone", "Company", "Service_Interest", "Amount (€)", 
            "VAT (€)", "Total_Amount (€)", "Invoice_Number", 
            "Priority", "Message"
        ));
        
        // Data rows
        for (Data item : dataToExport) {
            values.add(Arrays.asList(
                item.getType(),
                item.getSource(),
                item.getDate(),
                item.getClientName(),
                item.getEmail(),
                item.getPhone(),
                item.getCompany(),
                item.getServiceInterest(),
                item.getAmmount(),
                item.getVAT(),
                item.getTotalAmmount(),
                item.getInvoiceNumber(),
                item.getPriority(),
                item.getMessage().replace("\n", " ").replace("\r", " ").replace("\"", "\"\"")
            ));
        }
        
        ValueRange body = new ValueRange().setValues(values);
        sheetsService.spreadsheets().values()
            .update(createdSheet.getSpreadsheetId(), "A1", body)
            .setValueInputOption("RAW")
            .execute();
        
        
        // Redirect to new sheet
        response.sendRedirect("https://docs.google.com/spreadsheets/d/" + createdSheet.getSpreadsheetId());
    }
    
    


}
