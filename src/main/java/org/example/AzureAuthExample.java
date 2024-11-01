package org.example;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureAuthExample {
  private static final Logger logger = LoggerFactory.getLogger(AzureAuthExample.class);


  private static final String CLIENT_ID = System.getenv("CLIENT_ID_FWV");
  private static final String CLIENT_SECRET = System.getenv("CLIENT_SECRET_FWV");
  private static final String TENANT_ID = System.getenv("TENANT_ID");
  private static final String AUTHORITY = "https://login.microsoftonline.com/" + TENANT_ID;

  public static void main(String[] args) {

    if (CLIENT_ID == null || CLIENT_SECRET == null || TENANT_ID == null) {
      System.out.println(
          "Umgebungsvariablen CLIENT_ID, CLIENT_SECRET oder TENANT_ID sind nicht gesetzt.");
      return;
    }
    
    try {
      IClientCredential credential = ClientCredentialFactory.createFromSecret(CLIENT_SECRET);
      ConfidentialClientApplication app =
          ConfidentialClientApplication.builder(CLIENT_ID, credential)
              .authority(AUTHORITY)
              .build();

      Set<String> scopes = Collections.singleton("https://graph.microsoft.com/.default");
      ClientCredentialParameters parameters = ClientCredentialParameters.builder(scopes).build();

      CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);
      IAuthenticationResult result = future.join();

      logger.info("Access Token: {}", result.accessToken());
    } catch (MalformedURLException e) {
      logger.error("MalformedURLException occurred", e);
    } catch (Exception e) {
      logger.error("An error occurred", e);
    }
  }
}